package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 10:24
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object ZipAbout {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test").getOrCreate()
//    zip
//    def zip[U](other: RDD[U])(implicit arg0: ClassTag[U]): RDD[(T, U)]
//    zip函数用于将两个RDD组合成Key/Value形式的RDD,这里默认两个RDD的partition数量以及元素数量都相同，否则会抛出异常。
    println("zip=======")
    //var zipRdd1 = spark.sparkContext.makeRDD(1 to 10,2)
    var zipRdd1 = spark.sparkContext.makeRDD(1 to 5,2)
    var zipRdd2 = spark.sparkContext.makeRDD(Seq("A","B","C","D","E"),2)
    zipRdd1.zip(zipRdd2).collect.foreach(println)
    println("*******")
    zipRdd2.zip(zipRdd1).collect.foreach(println)
//    var zipRdd3 = spark.sparkContext.makeRDD(Seq("A","B","C","D","E"),3)//如果两个RDD分区数不同，则抛出异常
//    zipRdd1.zip(zipRdd3).collect


//    zipPartitions
//    zipPartitions函数将多个RDD按照partition组合成为新的RDD，该函数需要组合的RDD具有相同的分区数，但对于每个分区内的元素数量没有要求。
//    该函数有好几种实现，可分为三类：
//    参数是一个RDD
//    def zipPartitions[B, V](rdd2: RDD[B])(f: (Iterator[T], Iterator[B]) => Iterator[V])(implicit arg0: ClassTag[B], arg1: ClassTag[V]): RDD[V]
//    def zipPartitions[B, V](rdd2: RDD[B], preservesPartitioning: Boolean)(f: (Iterator[T], Iterator[B]) => Iterator[V])(implicit arg0: ClassTag[B], arg1: ClassTag[V]): RDD[V]
//    这两个区别就是参数preservesPartitioning，是否保留父RDD的partitioner分区信息
//    映射方法f参数为两个RDD的迭代器。
    println("zipPartitions=========")
    println("参数是一个RDD=========")
    var zipPartitionsRdd1 = spark.sparkContext.makeRDD(1 to 10,2)
    var zipPartitionsRdd2 = spark.sparkContext.makeRDD(Seq("A","B","C","D","E"),2)

    //rdd1两个分区中元素分布：
    zipPartitionsRdd1.mapPartitionsWithIndex{
               (x,iter) => {
                   var result = List[String]()
                     while(iter.hasNext){
                         result ::= ("part_" + x + "|" + iter.next())
                       }
                     result.iterator
                 }
             }.collect
    //rdd2两个分区中元素分布
    zipPartitionsRdd2.mapPartitionsWithIndex{
               (x,iter) => {
                   var result = List[String]()
                     while(iter.hasNext){
                         result ::= ("part_" + x + "|" + iter.next())
                       }
                     result.iterator
                 }
             }.collect
    //rdd1和rdd2做zipPartition
    zipPartitionsRdd1.zipPartitions(zipPartitionsRdd2){
             (rdd1Iter,rdd2Iter) => {
                 var result = List[String]()
                 while(rdd1Iter.hasNext && rdd2Iter.hasNext) {
                     result::=(rdd1Iter.next() + "_" + rdd2Iter.next())
                   }
                 result.iterator
               }
           }.collect.foreach(println)
//    参数是两个RDD
//    def zipPartitions[B, C, V](rdd2: RDD[B], rdd3: RDD[C])
//     (f: (Iterator[T], Iterator[B], Iterator[C]) => Iterator[V])(implicit arg0: ClassTag[B], arg1: ClassTag[C], arg2: ClassTag[V]): RDD[V]
//    def zipPartitions[B, C, V](rdd2: RDD[B], rdd3: RDD[C], preservesPartitioning:
//     Boolean)(f: (Iterator[T], Iterator[B], Iterator[C]) => Iterator[V])(implicit arg0: ClassTag[B], arg1: ClassTag[C], arg2: ClassTag[V]): RDD[V]
//    用法同上面，只不过该函数参数为两个RDD，映射方法f输入参数为两个RDD的迭代器
    println("***********")
    println("参数是两个RDD=========")
    var zipPartitionsRdd3 = spark.sparkContext.makeRDD(1 to 5,2)
    var zipPartitionsRdd4 = spark.sparkContext.makeRDD(Seq("A","B","C","D","E"),2)
    var zipPartitionsRdd5 = spark.sparkContext.makeRDD(Seq("a","b","c","d","e"),2)
    //三个RDD做zipPartitions
    var zipPartitionsRdd6 = zipPartitionsRdd3.zipPartitions(zipPartitionsRdd4,zipPartitionsRdd5){
             (rdd1Iter,rdd2Iter,rdd3Iter) => {
                 var result = List[String]()
                while(rdd1Iter.hasNext && rdd2Iter.hasNext && rdd3Iter.hasNext) {
                    result::=(rdd1Iter.next() + "_" + rdd2Iter.next() + "_" + rdd3Iter.next())
                   }
                 result.iterator
               }
           }
    zipPartitionsRdd6.collect().foreach(println)

//    参数是三个RDD
//    def zipPartitions[B, C, D, V](rdd2: RDD[B], rdd3: RDD[C], rdd4: RDD[D])(f: (Iterator[T], Iterator[B], Iterator[C], Iterator[D]) => Iterator[V])
//                                 (implicit arg0: ClassTag[B], arg1: ClassTag[C], arg2: ClassTag[D], arg3: ClassTag[V]): RDD[V]
//    def zipPartitions[B, C, D, V](rdd2: RDD[B], rdd3: RDD[C], rdd4: RDD[D], preservesPartitioning: Boolean)
//                                 (f: (Iterator[T], Iterator[B], Iterator[C], Iterator[D]) => Iterator[V])(implicit arg0: ClassTag[B], arg1: ClassTag[C], arg2: ClassTag[D], arg3: ClassTag[V]): RDD[V]
//    用法同上面，只不过这里又多了个一个RDD而已。
    println("**********")
//    zipWithIndex
//    def zipWithIndex(): RDD[(T, Long)]
//    该函数将RDD中的元素和这个元素在RDD中的ID（索引号）组合成键/值对。
    println("zipWithIndex======")
    var zipWithIndexRdd1 = spark.sparkContext.makeRDD(Seq("A","B","R","D","F"),2)
    zipWithIndexRdd1.zipWithIndex().collect.foreach(println)
//    zipWithUniqueId
//    def zipWithUniqueId(): RDD[(T, Long)]
//    该函数将RDD中元素和一个唯一ID组合成键/值对，该唯一ID生成算法如下：
//    每个分区中第一个元素的唯一ID值为：该分区索引号，
//    每个分区中第N个元素的唯一ID值为：(前一个元素的唯一ID值) + (该RDD总的分区数)
//    看下面的例子：
    println("zipWithUniqueId========")
    var zipWithUniqueIdRdd = spark.sparkContext.makeRDD(Seq("A","B","C","D","E","F"),2)
    zipWithUniqueIdRdd.zipWithUniqueId().collect.foreach(println)
    //总分区数为2
    //第一个分区第一个元素ID为0，第二个分区第一个元素ID为1
    //第一个分区第二个元素ID为0+2=2，第一个分区第三个元素ID为2+2=4
    //第二个分区第二个元素ID为1+2=3，第二个分区第三个元素ID为3+2=5






  }
}

