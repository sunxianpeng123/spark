package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/16
  * \* Time: 18:46
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object RandomGlom {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test").getOrCreate()
//    randomSplit
//    def randomSplit(weights: Array[Double], seed: Long = Utils.random.nextLong): Array[RDD[T]]
//    该函数根据weights权重，将一个RDD切分成多个RDD。
//    该权重参数为一个Double数组
//    第二个参数为random的种子，基本可忽略。

    println("randomSplit=============")
    var randomSplitRDD1 = spark.sparkContext.makeRDD(1 to 10,10)
    var splitRDD=randomSplitRDD1.randomSplit(Array(1.0,2.0,3.0,4.0))//这里注意：randomSplit的结果是一个RDD数组,
    // 由于randomSplit的第一个参数weights中传入的值有4个，因此，就会切分成4个RDD,
    //把原来的rdd按照权重1.0,2.0,3.0,4.0，随机划分到这4个RDD中，权重高的RDD，划分到//的几率就大一些。
    //注意，权重的总和加起来为1，否则会不正常
    splitRDD(0).collect().foreach(println)
    println("===========")
    splitRDD(1).collect().foreach(println)
    println("===========")
    splitRDD(2).collect().foreach(println)
    println("===========")
    splitRDD(3).collect().foreach(println)
    println("===========")
//    glom
//    def glom(): RDD[Array[T]]
//    该函数是将RDD中每一个分区中类型为T的元素转换成Array[T]，这样每一个分区就只有一个数组元素。
    var glomRDD = spark.sparkContext.makeRDD(1 to 10,3)
    println("glom============")
    println(glomRDD.partitions.size)//该RDD有3个分区
    glomRDD.collect().foreach(println)
    println("=============")
    glomRDD.glom().collect().foreach(x=>{x.foreach(println)
      println ("======")})
  }
}

