package com.xiaohulu.kafka_tool

//import kafka.common.TopicAndPartition
//import kafka.message.MessageAndMetadata
import com.xiaohulu.SparkExactlyOnceApp.sparkJdbcReadDao
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.{Decoder, StringDecoder}
import org.apache.spark.SparkException
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaCluster.LeaderOffset
import org.apache.spark.streaming.kafka.{KafkaCluster, KafkaUtils, OffsetRange}
//import org.apache.spark.streaming.kafka.KafkaCluster.LeaderOffset
//
import scala.reflect.ClassTag
import scala.util.Random

/**
  * Created by knowpigxia on 15-8-5.
  */
class KafkaManager(val kafkaParams: Map[String, String]) extends Serializable {

  private val kc = new KafkaCluster(kafkaParams)


  def createDirectStream[K: ClassTag, V: ClassTag, KD <: Decoder[K]: ClassTag, VD <: Decoder[V]: ClassTag](
                                                                                                            ssc: StreamingContext,
                                                                                                            kafkaParams: Map[String, String],
                                                                                                            topics: Set[String],
                                                                                                            sqlContext:SparkSession): InputDStream[(K, V)]
  =  {
    //    1、读取已存储的offset，若读到的数据为空，则表示第一次启动，否则 按照读到的offset 从kafka获取数据
    //    2、若读到offset不为空
    //        （1）在kafka增加分区时，无法感知到，需要从kafka获取对应信息（分区，topic，offset等），与已经存储的信息做对比
    //        （2）停止后，过了根据再启动，已经存储的offset太旧问题
    val offsetDF = sparkJdbcReadDao.get_kafka_offsetDF(sqlContext)
    offsetDF.show()
    val groupId = kafkaParams("group.id")
    //    // 在kafka上读取offsets前先根据实际情况更新offsets
    //    setOrUpdateOffsets(topics, groupId)

    var fromOffsets:Map[TopicAndPartition, Long]=offsetDF.rdd.map(row=>{(TopicAndPartition(row.getAs[String]("topic"),row.getAs[Int]("partition_id")),row.getAs[Long]("offset"))}).collect().toMap

    val kafkaDStream  = if (fromOffsets.isEmpty){
      //todo...第一次启动Streaming程序，从smallest开始运行
      println("=========第一次启动==============")
      KafkaUtils.createDirectStream[K, V, KD, VD](ssc, kafkaParams, topics)
    }else{
      //todo...非第一次启动Streaming程序
      //todo...解决自主存储kafka的offset过时问题
      //todo...解决程序无法主动感知 kafka 分区增加、topic增加的问题
      println("=========非第一次启动Streaming程序==============")
      fromOffsets = perceptKafkaChangeAndIndependentBehindTime(topics,groupId,fromOffsets)
//      val messageHandler= (mm:MessageAndMetadata[String, String])=>(mm.key(),mm.message())
      KafkaUtils.createDirectStream[K, V, KD, VD, (K, V)](ssc, kafkaParams,fromOffsets,(mmd: MessageAndMetadata[K, V]) => (mmd.key, mmd.message))
    }
    kafkaDStream
  }
  /**
    *感知kafka topic  和 分区变化。修复自主存储kafka offset信息过时问题
    * @param topics 主题
    * @param groupId 消费者组
    * @param fromOffsets  自主存储的offset信息
    * @return
    */
  private def perceptKafkaChangeAndIndependentBehindTime(topics: Set[String], groupId: String, fromOffsets:Map[TopicAndPartition,Long]): Map[TopicAndPartition,Long] = {
    var resMap :Map[TopicAndPartition,Long] = Map.empty
    topics.foreach(topic => {
      val partitionsE = kc.getPartitions(Set(topic))
      if (partitionsE.isLeft)
        throw new SparkException(s"get kafka partition failed: ${partitionsE.left.get}")
      val partitions = partitionsE.right.get
      /**
        * 如果streaming程序执行的时候出现kafka.common.OffsetOutOfRangeException，
        * 说明zk上保存的offsets已经过时了，即kafka的定时清理策略已经将包含该offsets的文件删除。
        * 针对这种情况，只要判断一下zk上的consumerOffsets和earliestLeaderOffsets的大小，
        * 如果consumerOffsets比earliestLeaderOffsets还小的话，说明consumerOffsets已过时,
        * 这时把consumerOffsets更新为earliestLeaderOffsets
        */
      val earliestLeaderOffsetsE = kc.getEarliestLeaderOffsets(partitions)
      if (earliestLeaderOffsetsE.isLeft)
        throw new SparkException(s"get earliest leader offsets failed: ${earliestLeaderOffsetsE.left.get}")
      val earliestLeaderOffsets = earliestLeaderOffsetsE.right.get
      // 可能只是存在部分分区consumerOffsets过时，所以只更新过时分区的consumerOffsets为earliestLeaderOffsets
      earliestLeaderOffsets.foreach({ case (tp, n) => {
        val earliestLeaderOffset = n.offset
        //todo...  kafka自主提交的topic和offset  包含  自主存储的topic和offset,对比自主提交的offset是否过时
        if (fromOffsets.contains(tp)) {
          val fromOffset = fromOffsets(tp)
          if (fromOffset < earliestLeaderOffset) {
            // 自主存储的offset 已经过时时
            println("consumer group:" + groupId + ",topic:" + tp.topic + ",partition:" + tp.partition + " offsets已经过时，更新为" + earliestLeaderOffset)
            resMap += (tp -> earliestLeaderOffset)
          }else{
            // 自主存储的offset 没有过时
            resMap += (tp -> fromOffset)
          }
        } else {
          //todo...  kafka自主提交的topic和offset  不包含  自主存储的topic和offset，将新增的kafka的topic 、partition 等信息添加到fromOffsets
          resMap += (tp -> earliestLeaderOffset)
        }
      }
      })
    })
    resMap
  }
  /**
    * 更新zookeeper上的消费offsets
    */
  def updateZKOffsets(offsetRanges : Array[OffsetRange]) : Unit = {
    val groupId = kafkaParams("group.id")
    val rnd = new Random()
    for (offsets <- offsetRanges) {
      val topicAndPartition = TopicAndPartition(offsets.topic, offsets.partition)
      val o = kc.setConsumerOffsets(groupId, Map((topicAndPartition, offsets.untilOffset)))
      if (o.isLeft) {
        println(s"Error updating the offset to Kafka cluster: ${o.left.get}")
      }
      if (rnd.nextInt(60) % 60 == 1) {
        println(s"更新groupId:${ groupId }, offset:${offsets.topic}, offset:${offsets.untilOffset}")
      }
    }
  }
}