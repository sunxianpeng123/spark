package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 11:27
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object PartitionByMapFlatValue {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    partitionBy
//    def partitionBy(partitioner: Partitioner): RDD[(K, V)]
//    该函数根据partitioner函数生成新的ShuffleRDD，将原RDD重新分区。
    println("partitionBy==========")
    var partitionByRdd1 = sc.makeRDD(Array((1,"A"),(2,"B"),(3,"C"),(4,"D")),2)
    println(partitionByRdd1.partitions.size)
    //查看rdd1中每个分区的元素
     partitionByRdd1.mapPartitionsWithIndex{
               (partIdx,iter) => {
                   var part_map = scala.collection.mutable.Map[String,List[(Int,String)]]()
                     while(iter.hasNext){
                         var part_name = "part_" + partIdx;
                         var elem = iter.next()
                         if(part_map.contains(part_name)) {
                             var elems = part_map(part_name)
                             elems ::= elem
                               part_map(part_name) = elems
                           } else {
                             part_map(part_name) = List[(Int,String)]{elem}
                           }
                       }
                     part_map.iterator

                 }
             }.collect.foreach(println)
    //使用partitionBy重分区
    println("==========")
    var partitionByRdd2 = partitionByRdd1.partitionBy(new org.apache.spark.HashPartitioner(2))
    println(partitionByRdd2.partitions.size)
    //查看rdd2中每个分区的元素
    partitionByRdd2.mapPartitionsWithIndex{
               (partIdx,iter) => {
                   var part_map = scala.collection.mutable.Map[String,List[(Int,String)]]()
                     while(iter.hasNext){
                         var part_name = "part_" + partIdx;
                         var elem = iter.next()
                         if(part_map.contains(part_name)) {
                             var elems = part_map(part_name)
                             elems ::= elem
                               part_map(part_name) = elems
                           } else {
                             part_map(part_name) = List[(Int,String)]{elem}
                           }
                       }
                     part_map.iterator
                 }
             }.collect.foreach(println)
//    mapValues
//    def mapValues[U](f: (V) => U): RDD[(K, U)]
//    同基本转换操作中的map，只不过mapValues是针对[K,V]中的V值进行map操作。
    println("mapValues===========")
    var mapValuesRdd1 = sc.makeRDD(Array((1,"A"),(1,"B"),(3,"C"),(4,"D")),2)
    mapValuesRdd1.mapValues(x => x + "_").collect.foreach(println)
//    flatMapValues
//    def flatMapValues[U](f: (V) => TraversableOnce[U]): RDD[(K, U)]
//    同基本转换操作中的flatMap，只不过flatMapValues是针对[K,V]中的V值进行flatMap操作。
//    flatMapValues类似于mapValues，不同的在于flatMapValues应用于元素为KV对的RDD中Value。每个一元素的Value被输入函数映射为一系列的值，然后这些值再与原RDD中的Key组成一系列新的KV对。
    println("flatMapValues======")
    mapValuesRdd1.flatMapValues(x => x + "_").collect.foreach(println)
    println("*******")
    mapValuesRdd1.flatMapValues(x=>x).collect().foreach(println)
    println("*******")
    val a = sc.parallelize(List((1,2),(3,4),(3,6)))
    val b = a.flatMapValues(x=>x.to(5))
    b.collect.foreach(println)//上述例子中原RDD中每个元素的值被转换为一个序列（从其当前值到5），
    // 比如第一个KV对(1,2), 其值2被转换为2，3，4，5。然后其再与原KV对中Key组成一系列新的KV对(1,2),(1,3),(1,4),(1,5)。


  }
}

