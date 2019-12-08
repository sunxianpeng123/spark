package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 11:20
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object StatPartitionsEleAndNum {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test").getOrCreate()
//    Spark RDD是被分区的，在生成RDD时候，一般可以指定分区的数量，如果不指定分区数量，当RDD从集合创建时候，
//    则默认为该程序所分配到的资源的CPU核数，如果是从HDFS文件创建，默认为文件的Block数。
//    可以利用RDD的mapPartitionsWithIndex方法来统计每个分区中的元素及数量。
//    具体看例子：
    //创建一个RDD，默认分区15个，因为我的spark-shell指定了一共使用15个CPU资源
    //–total-executor-cores 8
    var rdd1 = spark.sparkContext.makeRDD(1 to 50)
    println(rdd1.partitions.size)
    //统计rdd1每个分区中元素数量
    println("统计rdd1每个分区中元素数量======")
    rdd1.mapPartitionsWithIndex{
      (partIdx,iter) => {
        var part_map = scala.collection.mutable.Map[String,Int]()
        while(iter.hasNext){
          var part_name = "part_" + partIdx;
          if(part_map.contains(part_name)) {
            var ele_cnt = part_map(part_name)
            part_map(part_name) = ele_cnt + 1
          } else {
            part_map(part_name) = 1
          }
          iter.next()
        }
        part_map.iterator

      }
    }.collect.foreach(println)

    //统计rdd1每个分区中有哪些元素
    println("统计rdd1每个分区中有哪些元素======")
    rdd1.mapPartitionsWithIndex{
      (partIdx,iter) => {
        var part_map = scala.collection.mutable.Map[String,List[Int]]()
        while(iter.hasNext){
          var part_name = "part_" + partIdx;
          var elem = iter.next()
          if(part_map.contains(part_name)) {
            var elems = part_map(part_name)
            elems ::= elem
            part_map(part_name) = elems
          } else {
            part_map(part_name) = List[Int]{elem}
          }
        }
        part_map.iterator

      }
    }.collect.foreach(println)
    //从HDFS文件创建的RDD，包含65个分区，因为该文件由65个Block
//    var rdd2 = spark.sparkContext.textFile("/logs/2015-07-05/lxw1234.com.log")
//    println(rdd2.partitions.size)

  }
}

