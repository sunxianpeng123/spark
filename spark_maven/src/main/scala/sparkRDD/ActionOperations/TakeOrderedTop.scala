package sparkRDD.ActionOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 15:03
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object TakeOrderedTop {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    take
//    def take(num: Int): Array[T]
//    take用于获取RDD中从0到num-1下标的元素，不排序。
    println("take===========")
    var takeRdd1 = sc.makeRDD(Seq(10, 4, 2, 12, 3))
    takeRdd1.take(1).foreach(println)
    println("*********")
    takeRdd1.take(2).foreach(println)
//    top
//    def top(num: Int)(implicit ord: Ordering[T]): Array[T]
//    top函数用于从RDD中，按照默认（降序）或者指定的排序规则，返回前num个元素。
    println("top==========")
    var topRdd1 = sc.makeRDD(Seq(10, 4, 2, 12, 3))
    topRdd1.top(1).foreach(println)
    println("********")
    topRdd1.top(2).foreach(println)
    println(topRdd1.top(2).getClass)


//    takeOrdered
//    def takeOrdered(num: Int)(implicit ord: Ordering[T]): Array[T]
//    takeOrdered和top类似，只不过以和top相反的顺序返回元素。
    println("takeOrdered============")
    var takeOrderedRdd1 = sc.makeRDD(Seq(10, 4, 2, 12, 3))
    takeOrderedRdd1.top(1).foreach(println)
    println("***********")
    takeOrderedRdd1.top(2).foreach(println)
    println("***********")
    takeOrderedRdd1.takeOrdered(1).foreach(println)
    println("***********")
    takeOrderedRdd1.takeOrdered(2).foreach(println)



  }
}

