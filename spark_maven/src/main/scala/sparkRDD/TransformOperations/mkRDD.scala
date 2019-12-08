package sparkRDD.TransformOperations

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/16
  * \* Time: 11:07
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object mkRDD {
  def main(args: Array[String]): Unit = {
//    从集合创建RDD
//    parallelize
//    def parallelize[T](seq: Seq[T], numSlices: Int = defaultParallelism)(implicit arg0: ClassTag[T]): RDD[T]
//    从一个Seq集合创建RDD。
//    参数1：Seq集合，必须。
//    参数2：分区数，默认为该Application分配到的资源的CPU核数
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test").getOrCreate()
    println("parallelize =============")
    var rdd = spark.sparkContext.parallelize(1 to 10)
    println(rdd.partitions.size)
    var rdd2 = spark.sparkContext.parallelize(1 to 10,3)
    println(rdd2.partitions.size)
//    makeRDD
//    def makeRDD[T](seq: Seq[T], numSlices: Int = defaultParallelism)(implicit arg0: ClassTag[T]): RDD[T]
//    这种用法和parallelize完全相同
//    def makeRDD[T](seq: Seq[(T, Seq[String])])(implicit arg0: ClassTag[T]): RDD[T]
//    该用法可以指定每一个分区的preferredLocations。
    println("makeRDD =============")
    var collect = Seq((1 to 10,Seq("slave007.lxw1234.com","slave002.lxw1234.com")),
      (11 to 15,Seq("slave013.lxw1234.com","slave015.lxw1234.com")))
    var rdd3 = spark.sparkContext.makeRDD(collect)
    println(rdd3.preferredLocations(rdd3.partitions(0)))
    println(rdd3.preferredLocations(rdd3.partitions(1)))
//    从外部存储创建RDD
//    textFile
//    //从hdfs文件创建.
    println("testFile=============")
    //var rdd = spark.sparkContext.textFile("hdfs:///tmp/lxw1234/1.txt")
    //从本地文件创建
    //var rdd = spark.sparkContext.textFile("file:///etc/hadoop/conf/core-site.xml")
//    注意这里的本地文件路径需要在Driver和Executor端存在。
//    从其他HDFS文件格式创建
//    hadoopFile
//    sequenceFile
//    objectFile
//    newAPIHadoopFile
//    从Hadoop接口API创建
//    hadoopRDD
//    newAPIHadoopRDD
//    比如：从HBase创建RDD
    val conf = HBaseConfiguration.create()
    var hbaseRDD = spark.sparkContext.newAPIHadoopRDD(
      conf,classOf[org.apache.hadoop.hbase.mapreduce.TableInputFormat],classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],classOf[org.apache.hadoop.hbase.client.Result])
    hbaseRDD.count
  }
}

