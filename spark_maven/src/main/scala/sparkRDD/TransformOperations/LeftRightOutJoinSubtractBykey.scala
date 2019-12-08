package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 14:08
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object LeftRightOutJoinSubtractBykey {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    leftOuterJoin
//    def leftOuterJoin[W](other: RDD[(K, W)]): RDD[(K, (V, Option[W]))]
//    def leftOuterJoin[W](other: RDD[(K, W)], numPartitions: Int): RDD[(K, (V, Option[W]))]
//    def leftOuterJoin[W](other: RDD[(K, W)], partitioner: Partitioner): RDD[(K, (V, Option[W]))]
//    leftOuterJoin类似于SQL中的左外关联left outer join，返回结果以前面的RDD为主，关联不上的记录为空。只能用于两个RDD之间的关联，如果要多个RDD关联，多关联几次即可。
//    参数numPartitions用于指定结果的分区数
//    参数partitioner用于指定分区函数
    println("leftOuterJoin==========")
    var leftOuterJoinRdd1 = sc.makeRDD(Array(("A","1"),("B","2"),("C","3")),2)
    var leftOuterJoinRdd2 = sc.makeRDD(Array(("A","a"),("C","c"),("D","d")),2)
    leftOuterJoinRdd1.leftOuterJoin(leftOuterJoinRdd2).collect.foreach(println)
//    rightOuterJoin
//    def rightOuterJoin[W](other: RDD[(K, W)]): RDD[(K, (Option[V], W))]
//    def rightOuterJoin[W](other: RDD[(K, W)], numPartitions: Int): RDD[(K, (Option[V], W))]
//    def rightOuterJoin[W](other: RDD[(K, W)], partitioner: Partitioner): RDD[(K, (Option[V], W))]
//    rightOuterJoin类似于SQL中的有外关联right outer join，返回结果以参数中的RDD为主，关联不上的记录为空。只能用于两个RDD之间的关联，如果要多个RDD关联，多关联几次即可。
//    参数numPartitions用于指定结果的分区数
//    参数partitioner用于指定分区函数
    println("rightOuterJoin========")
    var rightOuterJoinRdd1 = sc.makeRDD(Array(("A","1"),("B","2"),("C","3")),2)
    var rightOuterJoinRdd2 = sc.makeRDD(Array(("A","a"),("C","c"),("D","d")),2)
    rightOuterJoinRdd1.rightOuterJoin(rightOuterJoinRdd2).collect.foreach(println)
//    subtractByKey
//    def subtractByKey[W](other: RDD[(K, W)])(implicit arg0: ClassTag[W]): RDD[(K, V)]
//    def subtractByKey[W](other: RDD[(K, W)], numPartitions: Int)(implicit arg0: ClassTag[W]): RDD[(K, V)]
//    def subtractByKey[W](other: RDD[(K, W)], p: Partitioner)(implicit arg0: ClassTag[W]): RDD[(K, V)]
//    subtractByKey和基本转换操作中的subtract类似
//    （http://blog.csdn.net/ljp812184246/article/details/53894861），只不过这里是针对K的，返回在主RDD中出现，并且不在otherRDD中出现的元素。
//    参数numPartitions用于指定结果的分区数
//    参数partitioner用于指定分区函数
    println("subtractByKey============")
    var subtractByKeyRdd1 = sc.makeRDD(Array(("A","1"),("B","2"),("C","3")),2)
    var subtractByKeyRdd2 = sc.makeRDD(Array(("A","a"),("C","c"),("D","d")),2)
    subtractByKeyRdd1.subtractByKey(subtractByKeyRdd2).collect.foreach(println)

  }
}

