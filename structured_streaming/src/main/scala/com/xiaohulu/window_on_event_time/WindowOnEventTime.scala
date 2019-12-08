package com.xiaohulu.window_on_event_time

import java.sql.Timestamp

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/8
  * \* Time: 18:14
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object WindowOnEventTime {
  case class TimeWord(word:String,timestamp:Timestamp)
  def main(args: Array[String]): Unit = {
    val window_size = 10//args(0)    //窗口长度
    //窗口滑动距离应当小于或等于窗口长度
    val slide_size = 5//if (args.length == 2) args(1).toInt else window_size
    if (slide_size > window_size) throw  new Exception("===wrong paragrams===")
    val windowDuration = s"$window_size seconds"
    val slideDuration = s"$slide_size seconds"

    Logger.getLogger("org").setLevel(Level.ERROR)
    val sqLContext = SparkSession.builder().appName(" event-time-window_App").master("local[*]")
      .getOrCreate()
    import sqLContext.implicits._
    val df = sqLContext.readStream.format("socket")
      .option("host","192.168.199.135")
      .option("port",9999)
      .option("includeTimestamp",true)//添加时间戳
      .load()//nc -lk 9999
    val words = df.as[(String,Timestamp)]
      .flatMap(line=>line._1.split(" ").map(word=> TimeWord(word,line._2))).toDF()
    import org.apache.spark.sql.functions._
    val wordCounts = words
      .groupBy(window(col("timestamp"),windowDuration,slideDuration),$"word")
      .count().orderBy("window")
    val query = wordCounts.writeStream.outputMode("complete")
      .format("console")
      .option("truncate","false")
      .start()
    query.awaitTermination()
  }
}

