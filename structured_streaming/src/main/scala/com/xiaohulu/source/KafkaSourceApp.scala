package com.xiaohulu.source
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/9
  * \* Time: 11:43
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object KafkaSourceApp {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sqLContext = SparkSession.builder().appName(" structured_streaming_kafka_App").master("local[*]")
      .getOrCreate()
    val df = sqLContext
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "Kafka-01:9092")
      .option("subscribe", "node-bullet-crawler-15")
      .load()


    val strDF = df
      .selectExpr( "CAST(value AS STRING)")
    val chatTypeName="chat"

    val query = strDF.writeStream.format("console")
      .option("checkpointLocation", "path/")
      .foreachBatch((df,id)=>{
        println(s"batchid = $id")
        import sqLContext.implicits._


        val transRDD  = KafkaStreamMapUtil.kafkaStreamToDStream(df,chatTypeName)
        val msgDF = transRDD.flatMap(x=>x)
          .flatMap(_._1)
          .map(e=>(e.platform_id,e.room_id,e.from_id,e.content,e.timestamp,e.date))
          .toDF("platform_id","room_id","from_id","content","timestamp","date")
          .selectExpr("*","platform_id as plat")
        val giftDF =transRDD.flatMap(x=>x)
          .flatMap(_._2)
          .map(e=>(e.platform_id,e.room_id,e.from_id,e.from_name,e.gift_type,e.price,e.count,e.timestamp,e.date,e.gift_name,e.gift_id))
          .toDF("platform_id","room_id","from_id","from_name","gift_type","price","count","timestamp","date","gift_name","gift_id")
          .selectExpr("*","platform_id as plat")
        msgDF.show()
        giftDF.show()
      }).start().awaitTermination()
//    println(query.lastProgress)
  }
}
