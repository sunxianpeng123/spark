package sparkRDD.ActionOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 15:50
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object CountByKeyForeachPartitionSortBy {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    countByKey
//    def countByKey(): Map[K, Long]
//    countByKey用于统计RDD[K,V]中每个K的数量。
    println("countByKey==============")
    var countByKeyRdd1 = sc.makeRDD(Array(("A",0),("A",2),("B",1),("B",2),("B",3)))
    val a =countByKeyRdd1.countByValue()
    println(a)
    println("*********")
    val b=countByKeyRdd1.countByKey()
    println(b)
//    foreach
//    def foreach(f: (T) ⇒ Unit): Unit
//    foreach用于遍历RDD,将函数f应用于每一个元素。
//    但要注意，如果对RDD执行foreach，只会在Executor端有效，而并不是Driver端。
//    比如：rdd.foreach(println)，只会在Executor的stdout中打印出来，Driver端是看不到的。
//    我在Spark1.4中是这样，不知道是否真如此。
//    这时候，使用accumulator共享变量与foreach结合，倒是个不错的选择。
    println("foreach=============")
    var cnt1 = sc.accumulator(0)
    var cnt2 = sc.accumulator(1)
    var foreachRdd1 = sc.makeRDD(1 to 10,2)
    foreachRdd1.foreach(x => cnt1 += x)
    foreachRdd1.foreach(x => cnt2 += x)
    println(cnt1.value)
    println(cnt2.value)
//    foreachPartition
//    def foreachPartition(f: (Iterator[T]) ⇒ Unit): Unit
//    foreachPartition和foreach类似，只不过是对每一个分区使用f。
    println("foreachPartition============")
    var foreachPartitionRdd1 = sc.makeRDD(1 to 10,2)
    var allsize = sc.accumulator(0)
    var foreachPartitionRdd2 = sc.makeRDD(1 to 10,2)
    foreachPartitionRdd1.foreachPartition { x => {
//            x.foreach(println)
             allsize += x.size
           }}
    println(allsize.value)
//    sortBy
//    def sortBy[K](f: (T) ⇒ K, ascending: Boolean = true, numPartitions: Int = this.partitions.length)(implicit ord: Ordering[K], ctag: ClassTag[K]): RDD[T]
//    sortBy根据给定的排序k函数将RDD中的元素进行排序。
    println("sortBy==========")
    var sortByRdd1 = sc.makeRDD(Seq(3,6,7,1,2,0),2)
    sortByRdd1.sortBy(x => x).collect.mkString(",").foreach(print)//默认升序
    println("\n*************")
    sortByRdd1.sortBy(x => x,false).collect.mkString(",").foreach(print)//降序
    //RDD[K,V]类型
    println("\n*************")
    var sortByRdd2 = sc.makeRDD(Array(("A",2),("A",1),("B",6),("B",3),("B",7)))
    //按照V进行升序排序
    sortByRdd2.sortBy(x => x).collect.mkString(",").foreach(print)
    println("\n*************")
    //按照V进行降序排序
    sortByRdd2.sortBy(x => x._2,false).collect.mkString(",").foreach(print)
    println("\n*************")
    sortByRdd2.sortBy(_._2,false).collect.mkString(",").foreach(print)
  }
}

