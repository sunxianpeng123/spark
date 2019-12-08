package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Dell on 2017/6/14.
  */
object TwoPairRdd {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf =new SparkConf().setMaster("local").setAppName("myapp")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")
//
    val rdd=sc.parallelize(List((1,2),(3,4),(3,6)))
    val other=sc.parallelize(List((3,9)))
//
    val result=rdd.subtractByKey(other)
    result.foreach(println)
//
    println("join::对两个RDD进行内连接")
    val result1=rdd.join(other)
    result1.foreach(println)
//
    println("right outer join::对两个RDD进行连接操作，确保第二个RDD的键必须存在（右外连接）")
    val result2=rdd.rightOuterJoin(other)
    result2.foreach(println)
//
    println("left Outer Join::对两个RDD进行连接操作，确保第一个RDD的键必须存在（左外连接）")
    val result3=rdd.leftOuterJoin(other)
    result3.foreach(println)
//
    println("cogroup::将两个rdd中拥有相同键的数据分组")
    val result4=rdd.cogroup(other)
    result4.foreach(println)

  }
}
