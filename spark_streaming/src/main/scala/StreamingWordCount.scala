import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/2/1
  * \* Time: 11:35
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object StreamingWordCount {
  /**
    * 实现按批次累加的功能
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val conf =new SparkConf().setAppName("streaming_word_count").setMaster("local[*]")
    val ssc =new StreamingContext(conf,Seconds(5))
    ssc.checkpoint("data/")
    /**
      * 在linux中，命令  nc -lk 和nc -lp  的区别是什么
      * nc
      * -l 开启 监听模式，用于指定nc将处于监听模式。通常 这样代表着为一个 服务等待客户端来链接指定的端口。
      * -p<通信端口> 设置本地主机使用的通信端口。有可能会关闭
      * -k<通信端口>强制 nc 待命链接.当客户端从服务端断开连接后，过一段时间服务端也会停止监听。 但通过选项 -k 我们可以强制服务器保持连接并继续监听端口。
      */
    //linux下执行    nc -lk 8888
    val dStream=ssc.socketTextStream("192.168.199.131",8888)
    val tuples:DStream[(String,Int)]=dStream.flatMap(_.split(" ")).map((_,1))
//按批次累加需要调用updateStateByKey方法
    val res =tuples.updateStateByKey(func,new HashPartitioner(ssc.sparkContext.defaultMinPartitions),false)
    res.print()

    ssc.start()
    ssc.awaitTermination()
  }
//三个参数的含义：word,当前批次单词次数，历史结果之前累加的结果
  val func =(it:Iterator[(String,Seq[Int],Option[Int])])=>{
    it.map(t=>{
      (t._1,t._2.sum + t._3.getOrElse(0))
    })

  }

}

