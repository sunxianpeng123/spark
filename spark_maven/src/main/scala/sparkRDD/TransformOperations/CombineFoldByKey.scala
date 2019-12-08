package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 13:24
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object CombineFoldByKey {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    combineByKey
//    def combineByKey[C](createCombiner: (V) => C, mergeValue: (C, V) => C, mergeCombiners: (C, C) => C): RDD[(K, C)]
//    def combineByKey[C](createCombiner: (V) => C, mergeValue: (C, V) => C, mergeCombiners: (C, C) => C, numPartitions: Int): RDD[(K, C)]
//    def combineByKey[C](createCombiner: (V) => C, mergeValue: (C, V) => C, mergeCombiners: (C, C) => C, partitioner: Partitioner, mapSideCombine: Boolean = true, serializer: Serializer = null): RDD[(K, C)]
//    该函数用于将RDD[K,V]转换成RDD[K,C],这里的V类型和C类型可以相同也可以不同。
//    其中的参数：
//    createCombiner：组合器函数，用于将V类型转换成C类型，输入参数为RDD[K,V]中的V,输出为C
//    mergeValue：合并值函数，将一个C类型和一个V类型值合并成一个C类型，输入参数为(C,V)，输出为C
//    mergeCombiners：合并组合器函数，用于将两个C类型值合并成一个C类型，输入参数为(C,C)，输出为C
//    numPartitions：结果RDD分区数，默认保持原有的分区数
//    partitioner：分区函数,默认为HashPartitioner
//    mapSideCombine：是否需要在Map端进行combine操作，类似于MapReduce中的combine，默认为true
//    看下面例子：
    println("combineByKey==========")
    var combineByKeyRdd1 = sc.makeRDD(Array(("A",1),("A",2),("B",1),("B",2),("C",1)))
    combineByKeyRdd1.combineByKey(
             (v : Int) => v + "_",
           (c : String, v : Int) => c + "@" + v,
           (c1 : String, c2 : String) => c1 + "$" + c2
         ).collect.foreach(println)
//    其中三个映射函数分别为：
//    createCombiner: (V) => C
//      (v : Int) => v + “_” //在每一个V值后面加上字符_，返回C类型(String)
//    mergeValue: (C, V) => C
//      (c : String, v : Int) => c + “@” + v //合并C类型和V类型，中间加字符@,返回C(String)
//    mergeCombiners: (C, C) => C
//      (c1 : String, c2 : String) => c1 + “$” + c2 //合并C类型和C类型，中间加$，返回C(String)
//    其他参数为默认值。
//    最终，将RDD[String,Int]转换为RDD[String,String]。
//    再看例子：
    println("**************")
    combineByKeyRdd1.combineByKey(
      (v : Int) => List(v),
      (c : List[Int], v : Int) => v :: c,
      (c1 : List[Int], c2 : List[Int]) => c1 ::: c2
    ).collect.foreach(println)//最终将RDD[String,Int]转换为RDD[String,List[Int]]。
//    foldByKey
//    def foldByKey(zeroValue: V)(func: (V, V) => V): RDD[(K, V)]
//    def foldByKey(zeroValue: V, numPartitions: Int)(func: (V, V) => V): RDD[(K, V)]
//    def foldByKey(zeroValue: V, partitioner: Partitioner)(func: (V, V) => V): RDD[(K, V)]
//    该函数用于RDD[K,V]根据K将V做折叠、合并处理，其中的参数zeroValue表示先根据映射函数将zeroValue应用于V,进行初始化V,再将映射函数应用于初始化后的V.
//      直接看例子：
    println("foldByKey==========")
    var foldByKeyRdd1 = sc.makeRDD(Array(("A",0),("A",2),("B",1),("B",2),("C",1)))
    foldByKeyRdd1.foldByKey(0)(_+_).collect.foreach(println)//将rdd1中每个key对应的V进行累加，注意zeroValue=0,需要先初始化V,映射函数为+操
    //作，比如("A",0), ("A",2)，先将zeroValue应用于每个V,得到：("A",0+0), ("A",2+0)，即：
    //("A",0), ("A",2)，再将映射函数应用于初始化后的V，最后得到(A,0+2),即(A,2)
    //再看乘法操作：
    println("**********")
    foldByKeyRdd1.foldByKey(0)(_*_).collect.foreach(println)//先将zeroValue=0应用于每个V,注意，这次映射函数为乘法，得到：("A",0*0), ("A",2*0)，
    //即：("A",0), ("A",0)，再将映射函//数应用于初始化后的V，最后得到：(A,0*0)，即：(A,0)
    //其他K也一样，最终都得到了V=0
    println("**********")
    foldByKeyRdd1.foldByKey(1)(_*_).collect.foreach(println)//映射函数为乘法时，需要将zeroValue设为1，才能得到我们想要的结果。
    /**
      * 在使用foldByKey算子时候，要特别注意映射函数及zeroValue的取值。
      */
  }
}

