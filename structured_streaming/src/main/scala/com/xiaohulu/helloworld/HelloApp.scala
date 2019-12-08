package com.xiaohulu.helloworld

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/8
  * \* Time: 16:25
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object HelloApp {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sqLContext = SparkSession.builder().appName(" HelloApp").master("local[*]")
      .getOrCreate()
    val df = sqLContext.readStream.format("socket")
      .option("host","192.168.199.135")
      .option("port",9999).load()//nc -lk 9999
    import sqLContext.implicits._
    val words = df.as[String].flatMap(_.split(" "))
    // 对 "value" 列做 count，得到多行二列的 Dataset/DataFrame；即 result table
    val wordCounts = words.groupBy("value").count()
    //操作流动的DataFrame（选择和投射）
    val operationDF=wordCounts.select("value").where("count>5")
    val query  = wordCounts.writeStream// 打算写出 wordCounts 这个 Dataset/DataFrame
//      .outputMode("complete") // 打算写出 wordCounts 的全量数据
      .outputMode("update") // 打算写出 wordCounts 的全量数据
      .format("console")// 打算写出到控制台
      .start()// 新起一个线程开始真正不停写出
    query.awaitTermination()// 当前用户主线程挂住，等待新起来的写出线程结束
  }
}

