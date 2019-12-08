package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

import scala.collection.mutable.ArrayBuffer

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/16
  * \* Time: 19:20
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object MapPartitionsAndWithIndex {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test").getOrCreate()
//    mapPartitions
//    def mapPartitions[U](f: (Iterator[T]) => Iterator[U], preservesPartitioning: Boolean = false)(implicit arg0: ClassTag[U]): RDD[U]
//    该函数和map函数类似，只不过映射函数的参数由RDD中的每一个元素变成了RDD中每一个分区的迭代器。如果在映射的过程中需要频繁创建额外的对象，使用mapPartitions要比map高效的过。
//    比如，将RDD中的所有数据通过JDBC连接写入数据库，如果使用map函数，可能要为每一个元素都创建一个connection，这样开销很大，如果使用mapPartitions，那么只需要针对每一个分区建立一个connection。
//    参数preservesPartitioning表示是否保留父RDD的partitioner分区信息。
    println("mapPartitions=========")
    var rdd1 = spark.sparkContext.makeRDD(1 to 5,2)
    rdd1.foreachPartition(row =>{
      while (row.hasNext){
        println(row.next())
      }
    })
    println("+++++++++++++++++++")
    var rdd2 = rdd1.mapPartitions{ x => {
      var result = List[Int]()
         var i = 0
         while(x.hasNext){
              i += x.next()
            }
          result.::(i).iterator
       }}//rdd2将rdd1中每个分区中的数值累加
    rdd2.collect().foreach(println)
    println("分区数======")
    println(rdd2.partitions.size)
    println("==========")
    println("rdd_arrBuffer+++++++++++++++++++")
    var rdd_arrBuffer = rdd1.mapPartitions{ x => {
      val seq =x.toSeq
      println(s"x.seq.length=${seq.length}")
      println(seq.length)
      var arrayBuffer = new ArrayBuffer[Int](x.length)
      while (x.hasNext){
        val num = x.next()
//        arrayBuffer.append(num)
        arrayBuffer += num
      }

      arrayBuffer.toArray.toIterator
    }}//rdd2将rdd1中每个分区中的数值累加
    println("result")
    rdd_arrBuffer.foreachPartition(part=>{
      while (part.hasNext){
        println(s"num = ${part.next()}")
      }
    })
    println("分区数======")
    println(rdd_arrBuffer.partitions.length)
    System.exit(0)
//    mapPartitionsWithIndex
//    def mapPartitionsWithIndex[U](f: (Int, Iterator[T]) => Iterator[U], preservesPartitioning: Boolean = false)(implicit arg0: ClassTag[U]): RDD[U]
//    函数作用同mapPartitions，不过提供了两个参数，第一个参数为分区的索引。
    println("mapPartitionsWithIndex=====")
    var rdd3 = spark.sparkContext.makeRDD(1 to 5,2)
    var rdd4 = rdd1.mapPartitionsWithIndex{
      (x,iter) => {
        var result = List[String]()
        var i = 0
        while(iter.hasNext){
          i += iter.next()
        }
        result.::(x + "|" + i).iterator

      }
    }//rdd2将rdd1中每个分区的数字累加，并在每个分区的累加结果前面加了分区索引

  rdd4.collect().foreach(println)

  }
}

