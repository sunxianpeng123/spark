package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/16
  * \* Time: 18:39
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object CoalesceRepartition {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test").getOrCreate()
//    coalesce
//    def coalesce(numPartitions: Int, shuffle: Boolean = false)(implicit ord: Ordering[T] = null): RDD[T]
//    该函数用于将RDD进行重分区，使用HashPartitioner。
//    第一个参数为重分区的数目，第二个为是否进行shuffle，默认为false;
    var data =spark.sparkContext.parallelize(List(1,2,3,4))
    println("coalesce=======")

    println(data.partitions.size)
    var coalesceRdd1=data.coalesce(7)
    println(coalesceRdd1.partitions.size)
    var coalesceRdd2=data.coalesce(4,true)
    println(coalesceRdd2.partitions.size)
//    repartition
//    def repartition(numPartitions: Int)(implicit ord: Ordering[T] = null): RDD[T]
//    该函数其实就是coalesce函数第二个参数为true的实现
    println("repartition===========")
    var repartitionRDD1=data.repartition(1)
    println(repartitionRDD1.partitions.size)
    var repartitionRDD2=data.repartition(4)
    println(repartitionRDD2.partitions.size)




  }
}

