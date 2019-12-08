package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 13:39
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object GroupReduceByKeyLocally {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    groupByKey
//    def groupByKey(): RDD[(K, Iterable[V])]
//    def groupByKey(numPartitions: Int): RDD[(K, Iterable[V])]
//    def groupByKey(partitioner: Partitioner): RDD[(K, Iterable[V])]
//    该函数用于将RDD[K,V]中每个K对应的V值，合并到一个集合Iterable[V]中，
//    参数numPartitions用于指定分区数；
//    参数partitioner用于指定分区函数；
    println("groupByKey========")
    var groupByKeyRdd1 = sc.makeRDD(Array(("A",0),("A",2),("B",1),("B",2),("C",1)))
    groupByKeyRdd1.groupByKey().collect().foreach(println)
//    reduceByKey
//    def reduceByKey(func: (V, V) => V): RDD[(K, V)]
//    def reduceByKey(func: (V, V) => V, numPartitions: Int): RDD[(K, V)]
//    def reduceByKey(partitioner: Partitioner, func: (V, V) => V): RDD[(K, V)]
//    该函数用于将RDD[K,V]中每个K对应的V值根据映射函数来运算。
//    参数numPartitions用于指定分区数；
//    参数partitioner用于指定分区函数；
    println("reduceByKey==========")
    var reduceByKeyRdd1 = sc.makeRDD(Array(("A",0),("A",2),("B",1),("B",2),("C",1)))
    println(reduceByKeyRdd1.partitions.size)
    var reduceByKeyRdd2 = reduceByKeyRdd1.reduceByKey((x,y) => x + y)
    println("**********")
    reduceByKeyRdd2.collect().foreach(println)
    println(reduceByKeyRdd2.partitions.size)
    println("***************")
    var reduceByKeyRdd3 =reduceByKeyRdd1.reduceByKey(new org.apache.spark.HashPartitioner(2),(x,y) => x + y)
    reduceByKeyRdd3.collect().foreach(println)
    println(reduceByKeyRdd3.partitions.size)
//    reduceByKeyLocally
//    def reduceByKeyLocally(func: (V, V) => V): Map[K, V]
//    该函数将RDD[K,V]中每个K对应的V值根据映射函数来运算，运算结果映射到一个Map[K,V]中，而不是RDD[K,V]。
    println("reduceByKeyLocally=========")
    var reduceByKeyLocallyRdd1 = sc.makeRDD(Array(("A",0),("A",2),("B",1),("B",2),("C",1)))
    reduceByKeyLocallyRdd1.reduceByKeyLocally((x,y) => x + y).foreach(kv=>{println(kv)})//运算结果映射到一个Map[K,V]中，而不是RDD[K,V]。


  }
}

