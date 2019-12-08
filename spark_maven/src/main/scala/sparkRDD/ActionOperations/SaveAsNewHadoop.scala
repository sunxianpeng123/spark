package sparkRDD.ActionOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 16:27
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object SaveAsNewHadoop {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    saveAsNewAPIHadoopFile
//    def saveAsNewAPIHadoopFile[F <: OutputFormat[K, V]](path: String)(implicit fm: ClassTag[F]): Unit
//    def saveAsNewAPIHadoopFile(path: String, keyClass: Class[_], valueClass: Class[_], outputFormatClass: Class[_ <: OutputFormat[_, _]], conf: Configuration = self.context.hadoopConfiguration): Unit
//    saveAsNewAPIHadoopFile用于将RDD数据保存到HDFS上，使用新版本Hadoop API。
//    用法基本同saveAsHadoopFile。
    println("saveAsNewAPIHadoopFile=========")
    import org.apache.spark.SparkConf
    import org.apache.spark.SparkContext
    import SparkContext._
    import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
    import org.apache.hadoop.io.Text
    import org.apache.hadoop.io.IntWritable

    var saveAsNewAPIHadoopFileRdd1 = sc.makeRDD(Array(("A",2),("A",1),("B",6),("B",3),("B",7)))
    saveAsNewAPIHadoopFileRdd1.saveAsNewAPIHadoopFile("/tmp/lxw1234/",classOf[Text],classOf[IntWritable],classOf[TextOutputFormat[Text,IntWritable]])

//    saveAsNewAPIHadoopDataset
//    def saveAsNewAPIHadoopDataset(conf: Configuration): Unit
//    作用同saveAsHadoopDataset,只不过采用新版本Hadoop API。
//    以写入HBase为例：
//    HBase建表：
//    create ‘lxw1234′,{NAME => ‘f1′,VERSIONS => 1},{NAME => ‘f2′,VERSIONS => 1},{NAME => ‘f3′,VERSIONS => 1}
//    完整的Spark应用程序：
    import org.apache.spark.SparkConf
    import org.apache.spark.SparkContext
    import SparkContext._
    import org.apache.hadoop.hbase.HBaseConfiguration
    import org.apache.hadoop.mapreduce.Job
    import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
    import org.apache.hadoop.hbase.io.ImmutableBytesWritable
    import org.apache.hadoop.hbase.client.Result
    import org.apache.hadoop.hbase.util.Bytes
    import org.apache.hadoop.hbase.client.Put

    object Test {
      def main(args : Array[String]) {
        val sparkConf = new SparkConf().setMaster("spark://lxw1234.com:7077").setAppName("lxw1234.com")
        val sc = new SparkContext(sparkConf);
        var rdd1 = sc.makeRDD(Array(("A",2),("B",6),("C",7)))

        sc.hadoopConfiguration.set("hbase.zookeeper.quorum ","zkNode1,zkNode2,zkNode3")
        sc.hadoopConfiguration.set("zookeeper.znode.parent","/hbase")
        sc.hadoopConfiguration.set(TableOutputFormat.OUTPUT_TABLE,"lxw1234")
        var job = new Job(sc.hadoopConfiguration)
        job.setOutputKeyClass(classOf[ImmutableBytesWritable])
        job.setOutputValueClass(classOf[Result])
        job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])

        rdd1.map(
          x => {
            var put = new Put(Bytes.toBytes(x._1))
            put.add(Bytes.toBytes("f1"), Bytes.toBytes("c1"), Bytes.toBytes(x._2))
            (new ImmutableBytesWritable,put)
          }
        ).saveAsNewAPIHadoopDataset(job.getConfiguration)

        sc.stop()
      }
    }
//    注意：保存到HBase，运行时候需要在SPARK_CLASSPATH中加入HBase相关的jar包。
//    可参考：http://lxw1234.com/archives/2015/07/332.htm
  }
}

