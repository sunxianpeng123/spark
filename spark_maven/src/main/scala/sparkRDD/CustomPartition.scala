package sparkRDD

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/12/20
  * \* Time: 9:59
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/

object CustomPartition {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf=new SparkConf()
      .setMaster("local[2]")
      .setAppName("TestMyParttioner")
      .set("spark.app.id","test-partition-id")
    val sc=new SparkContext(conf)
    val list=List(1,2,3,4,5)
    val pairRDD=sc.parallelize(List((1,"a"),(2,"b"),(3,"c"),(4,"d")))//注意：RDD一定要是key-value
    pairRDD.partitionBy(new MySparkPartition(4)).foreach(println)
    sc.stop()

  }
}
class MySparkPartition(numParts: Int) extends  Partitioner{
  override def numPartitions: Int = numParts
  /**
    * 可以自定义分区算法
    * @param key
    * @return
    * def numPartitions：这个方法需要返回你想要创建分区的个数；
    * def getPartition：这个函数需要对输入的key做计算，然后返回该key的分区ID，范围一定是0到numPartitions-1；
    * equals()：这个是Java标准的判断相等的函数，之所以要求用户实现这个函数是因为Spark内部会比较两个RDD的分区是否一样。
    */
  override def getPartition(key: Any): Int = {
    val part=key.toString.toInt%3
    println(key+"______"+part)
    part
  }
  override def equals(other: Any): Boolean = other match {
    case mypartition: MySparkPartition =>
      mypartition.numPartitions == numPartitions
    case _ =>
      false
  }
  override def hashCode: Int = numPartitions
}

