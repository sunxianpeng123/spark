package StructuredStreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/3/1
  * \* Time: 11:28
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object SparkStructedStreamingTest {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().appName("test").config("spark.some.config.option","some value").master("local[*]").getOrCreate()
    import spark.implicits._
    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "Kafka-01:9092")
      .option("fetch.message.max.bytes","20971520")
      .option("serializer.class","kafka.serializer.StringEncoder")
      .option("group.id" ,"tycoon-qa-stream_89050233213")
      .option("subscribe", "node-bullet-crawler-1")
      .load()

    val t =df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]
    t.show()

  }
}

