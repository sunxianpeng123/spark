package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 13:53
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object CogroupJoin {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    cogroup
//    ##参数为1个RDD
//    def cogroup[W](other: RDD[(K, W)]): RDD[(K, (Iterable[V], Iterable[W]))]
//    def cogroup[W](other: RDD[(K, W)], numPartitions: Int): RDD[(K, (Iterable[V], Iterable[W]))]
//    def cogroup[W](other: RDD[(K, W)], partitioner: Partitioner): RDD[(K, (Iterable[V], Iterable[W]))]
//    ##参数为2个RDD
//    def cogroup[W1, W2](other1: RDD[(K, W1)], other2: RDD[(K, W2)]): RDD[(K, (Iterable[V], Iterable[W1], Iterable[W2]))]
//    def cogroup[W1, W2](other1: RDD[(K, W1)], other2: RDD[(K, W2)], numPartitions: Int): RDD[(K, (Iterable[V], Iterable[W1], Iterable[W2]))]
//    def cogroup[W1, W2](other1: RDD[(K, W1)], other2: RDD[(K, W2)], partitioner: Partitioner): RDD[(K, (Iterable[V], Iterable[W1], Iterable[W2]))]
//    ##参数为3个RDD
//    def cogroup[W1, W2, W3](other1: RDD[(K, W1)], other2: RDD[(K, W2)], other3: RDD[(K, W3)]): RDD[(K, (Iterable[V], Iterable[W1], Iterable[W2], Iterable[W3]))]
//    def cogroup[W1, W2, W3](other1: RDD[(K, W1)], other2: RDD[(K, W2)], other3: RDD[(K, W3)], numPartitions: Int): RDD[(K, (Iterable[V], Iterable[W1], Iterable[W2], Iterable[W3]))]
//    def cogroup[W1, W2, W3](other1: RDD[(K, W1)], other2: RDD[(K, W2)], other3: RDD[(K, W3)], partitioner: Partitioner): RDD[(K, (Iterable[V], Iterable[W1], Iterable[W2], Iterable[W3]))]
//    cogroup相当于SQL中的全外关联full outer join，返回左右RDD中的记录，关联不上的为空。
//    参数numPartitions用于指定结果的分区数。
//    参数partitioner用于指定分区函数。
    println("cogroup===========")
//    ##参数为1个RDD的例子
    println("##参数为1个RDD的例子=====")
    var cogroupRdd1 = sc.makeRDD(Array(("A","1"),("B","2"),("C","3")),2)
    var cogroupRdd2 = sc.makeRDD(Array(("A","a"),("C","c"),("D","d")),2)
    println("*************")
    var cogroupRdd3=cogroupRdd1.cogroup(cogroupRdd2)
    println(cogroupRdd3.partitions.size)
    cogroupRdd3.collect().foreach(println)
    println("********")
    var cogroupRdd4=cogroupRdd1.cogroup(cogroupRdd2,3)
    println(cogroupRdd4.partitions.size)
    cogroupRdd4.collect().foreach(println)
//    ##参数为2个RDD的例子
    println("##参数为2个RDD的例子")
    var cogroupRdd5 = sc.makeRDD(Array(("A","1"),("B","2"),("C","3")),2)
    var cogroupRdd6 = sc.makeRDD(Array(("A","a"),("C","c"),("D","d")),2)
    var cogroupRdd7 = sc.makeRDD(Array(("A","A"),("E","E")),2)
    var cogroupRdd8 = cogroupRdd5.cogroup(cogroupRdd6,cogroupRdd7)
    println("************")
    println(cogroupRdd8.partitions.size)
    cogroupRdd8.collect().foreach(println)
//    ##参数为3个RDD示例略，同上。
//    join
//    def join[W](other: RDD[(K, W)]): RDD[(K, (V, W))]
//    def join[W](other: RDD[(K, W)], numPartitions: Int): RDD[(K, (V, W))]
//    def join[W](other: RDD[(K, W)], partitioner: Partitioner): RDD[(K, (V, W))]
//    join相当于SQL中的内关联join，只返回两个RDD根据K可以关联上的结果，join只能用于两个RDD之间的关联，如果要多个RDD关联，多关联几次即可。
//    参数numPartitions用于指定结果的分区数
//    参数partitioner用于指定分区函数
    println("join==========")
    var joinRdd1 = sc.makeRDD(Array(("A","1"),("B","2"),("C","3")),2)
    var joinRdd2 = sc.makeRDD(Array(("A","a"),("C","c"),("D","d")),2)
    joinRdd1.join(joinRdd2).collect.foreach(println)



  }
}

