package sparkRDD.ActionOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 16:16
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object SaveAsSomeFile {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    saveAsTextFile
//    def saveAsTextFile(path: String): Unit
//    def saveAsTextFile(path: String, codec: Class[_ <: CompressionCodec]): Unit
//    saveAsTextFile用于将RDD以文本文件的格式存储到文件系统中。
//    codec参数可以指定压缩的类名。
    var rdd1 = sc.makeRDD(1 to 10,2)
    rdd1.saveAsTextFile("hdfs://cdh5/tmp/lxw1234.com/") //保存到HDFS
//    hadoop fs -ls /tmp/lxw1234.com
//    Found 2 items
//      -rw-r--r--   2 lxw1234 supergroup        0 2015-07-10 09:15 /tmp/lxw1234.com/_SUCCESS
//    -rw-r--r--   2 lxw1234 supergroup        21 2015-07-10 09:15 /tmp/lxw1234.com/part-00000
//    hadoop fs -cat /tmp/lxw1234.com/part-00000
//    注意：如果使用rdd1.saveAsTextFile(“file:///tmp/lxw1234.com”)将文件保存到本地文件系统，那么只会保存在Executor所在机器的本地目录。
//    //指定压缩格式保存
//    rdd1.saveAsTextFile("hdfs://cdh5/tmp/lxw1234.com/",classOf[com.hadoop.compression.lzo.LzopCodec])
//    hadoop fs -ls /tmp/lxw1234.com
//    -rw-r--r--   2 lxw1234 supergroup    0 2015-07-10 09:20 /tmp/lxw1234.com/_SUCCESS
//    -rw-r--r--   2 lxw1234 supergroup    71 2015-07-10 09:20 /tmp/lxw1234.com/part-00000.lzo
//    hadoop fs -text /tmp/lxw1234.com/part-00000.lzo

    println("saveAsSequenceFile=========")

//    saveAsSequenceFile
//    saveAsSequenceFile用于将RDD以SequenceFile的文件格式保存到HDFS上。
//    用法同saveAsTextFile。

    println("saveAsObjectFile==========")
//    saveAsObjectFile
//    def saveAsObjectFile(path: String): Unit
//    saveAsObjectFile用于将RDD中的元素序列化成对象，存储到文件中。
//    对于HDFS，默认采用SequenceFile保存。
//    var rdd1 = sc.makeRDD(1 to 10,2)
//    rdd1.saveAsObjectFile("hdfs://cdh5/tmp/lxw1234.com/")
//    hadoop fs -cat /tmp/lxw1234.com/part-00000
//    SEQ !org.apache.hadoop.io.NullWritable"org.apache.hadoop.io.BytesWritableT



  }
}

