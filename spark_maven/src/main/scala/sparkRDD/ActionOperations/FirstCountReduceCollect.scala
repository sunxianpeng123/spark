package sparkRDD.ActionOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 14:17
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object FirstCountReduceCollect {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    first
//    def first(): T
//    first返回RDD中的第一个元素，不排序
    println("first==========")
    var firstRdd1 = sc.makeRDD(Array(("A","1"),("B","2"),("C","3")),2)
    println(firstRdd1.first())
    var firstRdd2 = sc.makeRDD(Seq(10, 4, 2, 12, 3))
    println(firstRdd2.first())
//    count
//    def count(): Long
//    count返回RDD中的元素数量。
    println("count==========")
    var countRdd1 = sc.makeRDD(Array(("A","1"),("B","2"),("C","3")),2)
    println(countRdd1.count())
//        reduce
//    def reduce(f: (T, T) ⇒ T): T
//    根据映射函数f，对RDD中的元素进行二元计算，返回计算结果。
    println("reduce========")
    var reduceRdd1 = sc.makeRDD(1 to 10,2)
    val a =reduceRdd1.reduce(_+_)
    println(a)
    println(a.getClass)
    println("optionReduce==========")
    println("不确定集合是否为空的情况下使用  reduceOption")
    val emptyList=List.empty[Int].reduceOption (_+_)
    val nonEmptyList=List(1,2,3,4,5,6).reduceOption(_+_)
    println("emptyList::"+emptyList)
    println("nonEmptyList::"+nonEmptyList)
    println("nonEmptyList::"+nonEmptyList.get)
    println("*************")
    var reduceRdd2 = sc.makeRDD(Array(("A",0),("A",2),("B",1),("B",2),("C",1)))
    val b=reduceRdd2.reduce((x,y) => {
             (x._1 + y._1,x._2 + y._2)
           })
    println(b)
//    collect
//    def collect(): Array[T]
//    collect用于将一个RDD转换成数组。
    println("collect===========")
    var collectRdd1 = sc.makeRDD(1 to 10,2)
    val c=collectRdd1.collect
    c.foreach(println)
    println(c.getClass)


  }
}

