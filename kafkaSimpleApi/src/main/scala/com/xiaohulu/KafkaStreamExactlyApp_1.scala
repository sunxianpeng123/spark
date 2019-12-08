package com.xiaohulu

import com.xiaohulu.pool.KafkaPool
import com.xiaohulu.tools.PropertyUtil
import control.handler
import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Hello world!
  *
  */
object KafkaStreamExactlyApp_1 {
  handler.propertyUtilInit()

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("streaming_word_count").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))
    //pargams
    val brokers = PropertyUtil.getString("k")
    //"Kafka-01:9092"//broker 集群
    val zookeeper = "Kafka-01111:2181"
    val sourceTopic = Set("15").map(x => ("node-bullet-crawler-" + x))
    val targetTopic = "target"
    val consumerGroup = "consumer_test"
    val kafkaParams = Map[String, String](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.GROUP_ID_CONFIG -> consumerGroup
    )
    val kafkaDStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, sourceTopic)
    kafkaDStream.foreachRDD(rdd => {
      rdd.foreachPartition(rddPart => {
        //创建生产者
        val kafkaPool = KafkaPool(brokers)
        val kafkaConn = kafkaPool.borrowObject()
        //写回kafka(targetTopic)
        rddPart.foreach(item=>{
          //      生产者发送数据
          kafkaConn.send(targetTopic,item._2)
        })
        //      关闭生产者
        kafkaPool.returnObject(kafkaConn)
      })
    })

//    val result = kafkaDStream.map(x => (x._1, x._2))
//    result.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
