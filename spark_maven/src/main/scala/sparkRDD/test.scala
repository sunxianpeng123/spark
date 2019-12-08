package sparkRDD

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/11/15
  * \* Time: 16:43
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object test {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    //转化
    val conf =new SparkConf().setMaster("local").setAppName("myapp")
    val sc = new SparkContext(conf)
    val sen=List("coffee panda","happy panda","happiest panda party")
    val t =List(List(("coffee ","panda"),("happy"," panda")),List(("happiest ","panda","party")))
    val sen1=sc.parallelize(t)
    sen1.reduce((x,y)=>x:::y).take(1).foreach(println)
    sen1.take(1).foreach(println)
//    sen1.reduce((x,y)=>x:::y).flatten.foreach(println)
//    val t1 =sen1.reduce((x,y)=>x:::y).flatten
    val t2 =sc.parallelize(List(("coffee ","panda"),("happy"," panda")))
    t2.foreach(println)
    t2.collectAsMap().foreach(kv=>println(kv._1+","+kv._2))

  }
}

