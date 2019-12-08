package com.xiaohulu.sink

import java.sql.Timestamp

import com.xiaohulu.window_on_event_time.WindowOnEventTime.TimeWord
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.streaming.{StreamingQueryListener, StreamingQueryProgress}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/8
  * \* Time: 16:25
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object ForeachSinkApp {
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
    var batchId: Long = 0
    //对查询添加一个监听，获取每个批次的处理信息
    sqLContext.streams.addListener(new StreamingQueryListener() {
      override def onQueryStarted(event: StreamingQueryListener.QueryStartedEvent): Unit = {}
      override def onQueryProgress(event: StreamingQueryListener.QueryProgressEvent): Unit = {
        val progress: StreamingQueryProgress = event.progress
        batchId = progress.batchId
        val inputRowsPerSecond: Double = progress.inputRowsPerSecond
        val processRowsPerSecond: Double = progress.processedRowsPerSecond
        val numInputRows: Long = progress.numInputRows
        println("batchId=" + batchId, "  numInputRows=" + numInputRows + "  inputRowsPerSecond=" + inputRowsPerSecond +
          "  processRowsPerSecond=" + processRowsPerSecond)
      }
      override def onQueryTerminated(event: StreamingQueryListener.QueryTerminatedEvent): Unit = {}
    })
    //使用structuredStreaming自带的Source产生数据
    //|-- timestamp: timestamp (nullable = true)
    // |-- value: long (nullable = true)
    val rateSource: DataFrame = sqLContext.readStream
      .format("rate")
      .option("rowsPerSecond", 100)
      .load()
    //增加一列 batchId
    val addDF: DataFrame = rateSource.as[(Timestamp, Long)].map(x => {
      val tuple: (Long, Timestamp, Long) = (batchId, x._1, x._2)
      tuple
    }).toDF("batchId","timestamp","num")
    //  过滤
    //    1.打印到控制台
    //    val resultDS: Dataset[Row] = addDF.filter("num%2=0")
    //    resultDS.writeStream.format("console")
    //      .foreachBatch((df,id)=>{
    //        println(s"batchid = $id")
    //        df.show()
    //      }).start().awaitTermination()
    //    2.写出到mysql
    val resultDS: Dataset[Row] = addDF.filter("num%2=0")
    resultDS.writeStream.format("update")
      .foreach(new MySqlSink)
      .start().awaitTermination()
  }
}

