package learn_sparkstreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}



/**
  * Created by Dell on 2017/7/10.
  */
object test {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    // 第一步：拿到SparkStreamning的上下文
    val conf =new SparkConf().setMaster("local[2]").setAppName("testStreaming")
    val ssc=new StreamingContext(conf,Seconds(2))
    //第二步，通过上下文拿到数据编写处理逻辑，这个返回的就是DStream
    val lines=ssc.socketTextStream("localhost",9999)
    val words=lines.flatMap(_.split(" "))
    val pairs=words.map(x=>(x,1))
    val wordsCounts=pairs.reduceByKey(_+_)
    wordsCounts.print()
    //第三步，启动上下文，并时刻监听执行的进度，只有启动了这个，才会真正触发计算
    println(1)
    ssc.start() //启动流式计算
    ssc.awaitTermination() //等待直到计算终止

  }
}
