package com.xiaohulu

import com.xiaohulu.control.handler
import dao.{MySqlConnect, SparkJdbcReadDao, SparkJdbcWriteDao}
import com.xiaohulu.tools.PropertyUtil
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/5/22
  * \* Time: 13:17
  * \* To change this template use File | Settings | File Templates.
  * \* Description: Spark Streaming 整合Kafka的偏移量管理
  * 1、这种方式是自我维护offset，可以做到一次语义。但是我们生产上有些业务线还是选择至少1次消费语义，选择Kafka自己维护offset。
  * 2、大数据流处理如何做到Exactly Once
  * \*/
object SparkOffsetApp {
  handler.propertyUtilInit()
  val sparkJdbcReadDao = new SparkJdbcReadDao
  sparkJdbcReadDao.init()
  val sparkJdbcWriteDao =new SparkJdbcWriteDao
  sparkJdbcWriteDao.init()
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("streaming_word_count").setMaster("local[*]")
    val sqlContext =SparkSession.builder().config(conf).getOrCreate()
    import sqlContext.implicits._
    val ssc = new StreamingContext(sqlContext.sparkContext, Seconds(5))
    //pargams
    val brokers = PropertyUtil.getString("k")    //"Kafka-01:9092"//broker 集群
    val sourceTopic:Set[String] = Set("15").map(x => "node-bullet-crawler-" + x)
    val consumerGroup = "consumer_test1111"
    //    val kafkaParams = Map[String, String](
    //      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
    //      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
    //      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
    //      ConsumerConfig.GROUP_ID_CONFIG -> consumerGroup
    //    )
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokers,
      "group.id" -> consumerGroup,
      "fetch.message.max.bytes" -> "20971520",
      "auto.offset.reset"->"smallest"//smallest lastest  largest
    )
    //接收kafka数据
    //    =============================>获取offset
    //  TODO...获取偏移量，
    //  TODO...mysql创建一张表 存放offset，字段：topic（主题）、group_id（消费者组）、partition（分区）、offset（偏移量）,datetime(时间)
    //  TODO...offset可以存放 在 Zookeeper、mysql、kafka、hbase、redis等。。。
    val offsetDF = sparkJdbcReadDao.get_kafka_offsetDF(sqlContext)
    offsetDF.printSchema()
    offsetDF.show()
    val fromOffsets:Map[TopicAndPartition, Long]=offsetDF.rdd.map(row=>{
      (TopicAndPartition(row.getAs[String]("topic"),row.getAs[Int]("partition_id")),row.getAs[Long]("offset"))
    }).collect().toMap

    val kafkaDStream =  if (fromOffsets.isEmpty){//第一次开始运行，从smallest开始运行
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, sourceTopic)
      //业务逻辑处理
    }else{//已经处理过的，（已存在offset）
    val messageHandler= (mm:MessageAndMetadata[String, String])=>(mm.key(),mm.message())
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder,(String,String)](ssc, kafkaParams,fromOffsets,messageHandler)
    }

    kafkaDStream.foreachRDD { rdd =>
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      println(".....................############################")
//考虑将 offset和 数据 合并在一起操作  ?????
      if (!rdd.isEmpty) {
        rdd.foreachPartition(part=>{
          val o = offsetRanges(TaskContext.get.partitionId)
          println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
          part.foreach(msg=>{
//            println(msg)
          })
          println(".....................############################")
          //          TODO...保存offset
          val mySqlConnect = new MySqlConnect
          mySqlConnect.connect()
          mySqlConnect.insertKafka_offset(o,consumerGroup)
        })
      } else {
        println("no data this batch !!!")
      }
      println("=============================")
    }






    ssc.start()
    ssc.awaitTermination()

  }
}

