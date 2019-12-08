package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/13
  * \* Time: 14:33
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object DistinctFlatMap {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test").getOrCreate()
    println("RDD基本转换操作(1)–map、flagMap、distinct ")
    val mapFlatmapDistinct=spark.sparkContext.parallelize(List("hello world"," hello spark ", "hello hive"))
    println("map =====")
    //    map
    //    将一个RDD中的每个数据项，通过map中的函数映射变为一个新的元素。
    //    输入分区与输出分区一对一，即：有多少个输入分区，就有多少个输出分区。
    val map=mapFlatmapDistinct.map(x=>x.split(" "))
    map.foreach(println)
    map.foreach(x=>{x.foreach(println)})
    println("flatmap =====")
//    flatMap
//    属于Transformation算子，第一步和map一样，最后将所有的输出分区合并成一个。
    val flatmap=mapFlatmapDistinct.flatMap(x=>x.split(","))
    flatmap.collect().foreach(println)
//    distinct
//    对RDD中的元素进行去重操作。
    val distinctRDD=mapFlatmapDistinct.flatMap(line=>line.split(" ")).distinct()
    println("distinct=======")
    distinctRDD.foreach(println)
  }
}

