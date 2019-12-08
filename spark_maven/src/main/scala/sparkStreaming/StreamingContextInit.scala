package sparkStreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingContextInit {
  /**
    * 1、Spark Streaming 是 Spark Core API 的扩展，它支持弹性的，高吞吐的，容错的实时数据流的处理。
    * 数据可以通过多种数据源获取，例如 Kafka，Flume，Kinesis 以及 TCP sockets，
    * 也可以通过例如 map，reduce，join，window 等的高阶函数组成的复杂算法处理。
    * 最终，处理后的数据可以输出到文件系统，数据库以及实时仪表盘中。
    * 事实上，你还可以在数据流上使用 Spark机器学习 以及 图形处理算法 。
    *
    * 2、Spark Streaming 提供了一个高层次的抽象叫做离散流（discretized stream）或者 DStream，
    * 它代表一个连续的数据流。 DStream 可以通过来自数据源的输入数据流创建，
    * 例如 Kafka，Flume 以及 Kinesis，或者在其他 DStream 上进行高层次的操作创建。
    * 在内部，一个 DStream 是通过一个 RDD 的序列来表示。
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    //一个 StreamingContext 对象可以从一个 SparkConf 对象中创建出来。
    val conf = new SparkConf().setMaster("local[*]").setAppName("test")
    val ssc= new StreamingContext(conf,Seconds(1))
    //一个 StreamingContext 对象也可以从一个现有的 SparkContext 对象中创建出。
//    val sc=new SparkContext(conf)
//    val ssc_sc=new StreamingContext(sc,Seconds(1))


    val lines =ssc.socketTextStream("localhost",8080)
    val words=lines.flatMap(_.split(" "))
    val pairs=words.map(word=>(word,1))
    val wordsCounts=pairs.reduceByKey(_+_)
    wordsCounts.print()

    //请注意，当这些行（lines）被执行的时候， Spark Streaming 只有建立在启动时才会执行计算,
    println(ssc.sparkContext)
    ssc.start()
    ssc.awaitTermination()
    /**
      * 1、一个 context 定义之后，你必须做以下几个方面。
      * 通过创建输入 DStreams 定义输入源。
      * 通过应用转换和输出操作 DStreams 定义流计算（streaming computations）。
      * 开始接收数据，并用 streamingContext.start() 处理它。
      * 等待处理被停止（手动停止或者因为任何错误停止）使用 StreamingContext.awaitTermination() 。
      * 该处理可以使用 streamingContext.stop() 手动停止。
      * 2、要记住的要点 :
      * 一旦一个 context 已经启动，将不会有新的数据流的计算可以被创建或者添加到它。
      * 一旦一个 context 已经停止，它不会被重新启动。
      * 同一时间内在 JVM 中只有一个 StreamingContext 可以被激活。
      * 在 StreamingContext 上的 stop() 同样也停止了 SparkContext 。为了只停止 StreamingContext ，设置 stop() 的可选参数，名叫 stopSparkContext 为 false 。
      * 一个 SparkContext 可以被重新用于创建多个 StreamingContexts，只要前一个 StreamingContext 在下一个StreamingContext 被创建之前停止（不停止 SparkContext）。
      */


  }

}
