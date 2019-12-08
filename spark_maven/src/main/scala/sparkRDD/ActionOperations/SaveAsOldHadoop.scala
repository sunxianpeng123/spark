package sparkRDD.ActionOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 16:21
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object SaveAsOldHadoop {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    saveAsHadoopFile
//    def saveAsHadoopFile(path: String, keyClass: Class[_], valueClass: Class[_], outputFormatClass: Class[_ <: OutputFormat[_, _]], codec: Class[_ <: CompressionCodec]): Unit
//    def saveAsHadoopFile(path: String, keyClass: Class[_], valueClass: Class[_], outputFormatClass: Class[_ <: OutputFormat[_, _]], conf: JobConf = …, codec: Option[Class[_ <: CompressionCodec]] = None): Unit
//    saveAsHadoopFile是将RDD存储在HDFS上的文件中，支持老版本Hadoop API。
//    可以指定outputKeyClass、outputValueClass以及压缩格式。
//    每个分区输出一个文件。
    println("saveAsHadoopFile========")
    var saveAsHadoopFileRdd1 = sc.makeRDD(Array(("A",2),("A",1),("B",6),("B",3),("B",7)))
    import org.apache.hadoop.mapred.TextOutputFormat
    import org.apache.hadoop.io.Text
    import org.apache.hadoop.io.IntWritable
    saveAsHadoopFileRdd1.saveAsHadoopFile("/tmp/lxw1234.com/",classOf[Text],classOf[IntWritable],classOf[TextOutputFormat[Text,IntWritable]])
//    saveAsHadoopFileRdd1.saveAsHadoopFile("/tmp/lxw1234.com/",classOf[Text],classOf[IntWritable],classOf[TextOutputFormat[Text,IntWritable]],
//      classOf[com.hadoop.compression.lzo.LzopCodec])
//    saveAsHadoopDataset
//    def saveAsHadoopDataset(conf: JobConf): Unit
//    saveAsHadoopDataset用于将RDD保存到除了HDFS的其他存储中，比如HBase。
//    在JobConf中，通常需要关注或者设置五个参数：
//    文件的保存路径、key值的class类型、value值的class类型、RDD的输出格式(OutputFormat)、以及压缩相关的参数。
//    ##使用saveAsHadoopDataset将RDD保存到HDFS中
    println("saveAsHadoopDataset=========")
    import org.apache.spark.SparkConf
    import org.apache.spark.SparkContext
    import SparkContext._
    import org.apache.hadoop.mapred.TextOutputFormat
    import org.apache.hadoop.io.Text
    import org.apache.hadoop.io.IntWritable
    import org.apache.hadoop.mapred.JobConf
    var saveAsHadoopDatasetRdd1 = sc.makeRDD(Array(("A",2),("A",1),("B",6),("B",3),("B",7)))
    var jobConf = new JobConf()
    jobConf.setOutputFormat(classOf[TextOutputFormat[Text,IntWritable]])
    jobConf.setOutputKeyClass(classOf[Text])
    jobConf.setOutputValueClass(classOf[IntWritable])
    jobConf.set("mapred.output.dir","/tmp/lxw1234/")
    saveAsHadoopDatasetRdd1.saveAsHadoopDataset(jobConf)
//    结果：
//    hadoop fs -cat /tmp/lxw1234/part-00000
//    A       2
//    A       1
//    hadoop fs -cat /tmp/lxw1234/part-00001
//    B       6
//    B       3
//    B       7
//    ##保存数据到HBASE
//    HBase建表：
//    create ‘lxw1234′,{NAME => ‘f1′,VERSIONS => 1},{NAME => ‘f2′,VERSIONS => 1},{NAME => ‘f3′,VERSIONS => 1}
    import org.apache.spark.SparkConf
    import org.apache.spark.SparkContext
    import SparkContext._
    import org.apache.hadoop.mapred.TextOutputFormat
    import org.apache.hadoop.io.Text
    import org.apache.hadoop.io.IntWritable
    import org.apache.hadoop.mapred.JobConf
    import org.apache.hadoop.hbase.HBaseConfiguration
    import org.apache.hadoop.hbase.mapred.TableOutputFormat
    import org.apache.hadoop.hbase.client.Put
    import org.apache.hadoop.hbase.util.Bytes
    import org.apache.hadoop.hbase.io.ImmutableBytesWritable

    var conf = HBaseConfiguration.create()
    var jobConf1 = new JobConf(conf)
    jobConf1.set("hbase.zookeeper.quorum","zkNode1,zkNode2,zkNode3")
    jobConf1.set("zookeeper.znode.parent","/hbase")
    jobConf1.set(TableOutputFormat.OUTPUT_TABLE,"lxw1234")
    jobConf1.setOutputFormat(classOf[TableOutputFormat])

    var rdd1 = sc.makeRDD(Array(("A",2),("B",6),("C",7)))
    rdd1.map(x =>
    {
      var put = new Put(Bytes.toBytes(x._1))
      put.add(Bytes.toBytes("f1"), Bytes.toBytes("c1"), Bytes.toBytes(x._2))
      (new ImmutableBytesWritable,put)
    }
    ).saveAsHadoopDataset(jobConf)
//
//    ##结果：
//    hbase(main):005:0> scan 'lxw1234'
//    ROW     COLUMN+CELL
//    A       column=f1:c1, timestamp=1436504941187, value=\x00\x00\x00\x02
//    B       column=f1:c1, timestamp=1436504941187, value=\x00\x00\x00\x06
//    C       column=f1:c1, timestamp=1436504941187, value=\x00\x00\x00\x07
//    3 row(s) in 0.0550 seconds

//    注意：保存到HBase，运行时候需要在SPARK_CLASSPATH中加入HBase相关的jar包。
//    可参考：http://lxw1234.com/archives/2015/07/332.htm



  }
}

