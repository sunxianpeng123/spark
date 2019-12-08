package com.xiaohulu

import com.xiaohulu.control.handler
import com.xiaohulu.dao.{MySqlConnect, SparkJdbcReadDao, SparkJdbcWriteDao}
import com.xiaohulu.kafka_tool.{KafkaManager, KafkaStreamMap}
import com.xiaohulu.tools.{Helper, PropertyUtil}
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, TaskContext}

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/5/22
  * \* Time: 13:17
  * \* To change this template use File | Settings | File Templates.
  * \* Description: Spark Streaming 整合Kafka的偏移量管理
  * 1、这种方式是自我维护offset，可以做到至少一次（at least once）语义。
  * 2、需要实现 exactly once 语义 可以采取如下方法
  *     （1）、提交offset 和 存储计算结果 放在 事务中执行，这个方法需要 数据库支持事务，并且 offset 和 数据计算结果 需要存放在同一个数据库中。
  *     （2）、下游数据库支持幂等操作，对同一数据运行多次产生的结果时一样的。
  * 3、解决在如下三种状况下，可能出现的bug：
  *     （1）第一次启动
  *     （2）项目停止后再次启动，若过了很久再启动，可能会出现已经存储的offset 太旧，导致报错
  *     （3）kafka 的topic 增加分区
  * // TODO...本例子可以做到Exactly Once，个人认为可能会出现问题，因为加入了动态感应kafka变化，可能造成与自主存储offset匹配出现问题，
  * // TODO...所以当kafka变化时，个人认为应该吧 动态感知去掉，手动将kafka的topic和partition变化更新到mysql
  * \*/
object SparkExactlyOnceApp {
  handler.propertyUtilInit()
  val sparkJdbcReadDao = new SparkJdbcReadDao
  sparkJdbcReadDao.init()
  val sparkJdbcWriteDao =new SparkJdbcWriteDao
  sparkJdbcWriteDao.init()
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("streaming_word_count").setMaster("local[*]")
    val sqlContext =SparkSession.builder().config(conf).getOrCreate()
    val ssc = new StreamingContext(sqlContext.sparkContext, Seconds(5))
    sqlContext.sparkContext.setLogLevel("ERROR")
    //pargams
    val brokers = PropertyUtil.getString("k")
    val topics:Set[String] = Set("node-bullet-crawler-59")
    val consumerGroup = "consumer_test1111"
    val chatTypeName="chat"
    //todo...注意：在kafka 客户端0.8中"auto.commit.enable"-> "false"，在kafka 客户端0.9+中"enable.auto.commit"-> "false"
    //todo... 这种方式需要将 自动提交offset 设置为true
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers, "group.id" -> consumerGroup, "fetch.message.max.bytes" -> "20971520", "auto.commit.enable"-> "true","auto.offset.reset"->"largest")
    val kafkaManager = new KafkaManager(kafkaParams)
    val kafkaDStream = kafkaManager.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaParams,topics,sqlContext)
    val events = KafkaStreamMap.kafkaStreamToDStream(kafkaDStream,chatTypeName)


    /**
      * action
      */
    try {
      events.foreachRDD(rdd=>{
        val offsetRanges =KafkaStreamMap.offsetRanges
        if(rdd!=null) {
          val m = rdd.filter(_!=null).flatMap(x=>x).filter( _._1  != null).flatMap(_._1).filter(_!=null)
          val g = rdd.filter(_!=null).flatMap(x=>x).filter(_._2 != null).flatMap(_._2).filter(_!=null)
          println(s"------message count =${m.count()},,,gift count = ${g.count()}---handlerData start  " + Helper.getNowDate() + "------")
          val sqlContext = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
          //          rdd.foreachPartition(part=> {
          //            val o = offsetRanges(TaskContext.get.partitionId)
          //            println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
          //          })
          if (m.count() > 0 ){
            val msgArray =m.collect()
            val mySqlConnect = new MySqlConnect
            mySqlConnect.connect()
            mySqlConnect.insertMsgInfoKafkaOffset(msgArray,offsetRanges,consumerGroup)
          }
          println("---------handlerData end" + Helper.getNowDate() + "--------------")
        }else{
          println("no data this batch !!!")
        }
      })
    }
    catch {
      case e: Exception =>println(e.getMessage)
    }
    ssc.start()
    ssc.awaitTermination()

  }
}

