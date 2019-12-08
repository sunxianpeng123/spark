package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/16
  * \* Time: 19:02
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object UnionIntersectionSubtract {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test").getOrCreate()
//    union
//    def union(other: RDD[T]): RDD[T]
//    该函数比较简单，就是将两个RDD进行合并，不去重。
    println("union========")
    var rdd1 = spark.sparkContext.makeRDD(1 to 2,1)
    var rdd2 = spark.sparkContext.makeRDD(2 to 3,1)
    rdd1.union(rdd2).collect().foreach(println)
//    intersection
//    def intersection(other: RDD[T]): RDD[T]
//    def intersection(other: RDD[T], numPartitions: Int): RDD[T]
//    def intersection(other: RDD[T], partitioner: Partitioner)(implicit ord: Ordering[T] = null): RDD[T]
//    该函数返回两个RDD的交集，并且去重。
//    参数numPartitions指定返回的RDD的分区数。
//    参数partitioner用于指定分区函数
      println("intersection======")
    var rdd4 = spark.sparkContext.makeRDD(1 to 2,1)
    var rdd5 = spark.sparkContext.makeRDD(2 to 3,1)

    rdd4.intersection(rdd5).collect().foreach(println)
    println("==========")
    var rdd6=rdd4.intersection(rdd5)
    println(rdd6.partitions.size)
    println("===============")
    var rdd7 = rdd4.intersection(rdd5,2)
    println(rdd7.partitions.size)
    rdd7.collect().foreach(println)

//    subtract
//    def subtract(other: RDD[T]): RDD[T]
//    def subtract(other: RDD[T], numPartitions: Int): RDD[T]
//    def subtract(other: RDD[T], partitioner: Partitioner)(implicit ord: Ordering[T] = null): RDD[T]
//    该函数类似于intersection，但返回在RDD中出现，并且不在otherRDD中出现的元素，不去重。
//    参数含义同intersection
    println("subtract==============")
    var rdd8 = spark.sparkContext.makeRDD(Seq(1,2,2,3))
    var rdd9=spark.sparkContext.parallelize(3 to 4)
    rdd8.subtract(rdd9).collect.foreach(println)
  }
}

