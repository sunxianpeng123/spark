package sparkRDD.ActionOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 15:25
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object AggregateFoldLookUp {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("test").getOrCreate().sparkContext
//    aggregate
//    def aggregate[U](zeroValue: U)(seqOp: (U, T) ⇒ U, combOp: (U, U) ⇒ U)(implicit arg0: ClassTag[U]): U
//    aggregate用户聚合RDD中的元素，先使用seqOp将RDD中每个分区中的T类型元素聚合成U类型
//    ，再使用combOp将之前每个分区聚合后的U类型聚合成U类型，特别注意seqOp和combOp都会使用zeroValue的值，zeroValue的类型为U。
    println("aggregate========")
    var aggregateRdd1 = sc.makeRDD(1 to 10,2)
    aggregateRdd1.mapPartitionsWithIndex{
      (partIdx,iter) => {
        var part_map = scala.collection.mutable.Map[String,List[Int]]()
        while(iter.hasNext){
          var part_name = "part_" + partIdx;
          var elem = iter.next()
          if(part_map.contains(part_name)) {
            var elems = part_map(part_name)
            elems ::= elem
            part_map(part_name) = elems
          } else {
            part_map(part_name) = List[Int]{elem}
          }
        }
        part_map.iterator

      }
    }.collect.foreach(println)
    println("***********")
    val a =aggregateRdd1.aggregate(1)(
                 {(x : Int,y : Int) => x + y},
                 {(a : Int,b : Int) => a + b}
             )
    println(a)
//    结果为什么是58，看下面的计算过程：
//    ##先在每个分区中迭代执行 (x : Int,y : Int) => x + y 并且使用zeroValue的值1
//    ##即：part_0中 zeroValue+5+4+3+2+1 = 1+5+4+3+2+1 = 16
//    ## part_1中 zeroValue+10+9+8+7+6 = 1+10+9+8+7+6 = 41
//    ##再将两个分区的结果合并(a : Int,b : Int) => a + b ，并且使用zeroValue的值1
//    ##即：zeroValue+part_0+part_1 = 1 + 16 + 41 = 58
//    再比如：
//        ##这次zeroValue=2
//        ##part_0中 zeroValue+5+4+3+2+1 = 2+5+4+3+2+1 = 17
//        ##part_1中 zeroValue+10+9+8+7+6 = 2+10+9+8+7+6 = 42
//        ##最后：zeroValue*part_0*part_1 = 2 * 17 * 42 = 1428
//        因此，zeroValue即确定了U的类型，也会对结果产生至关重要的影响，使用时候要特别注意。
    println("***********")
    val b =aggregateRdd1.aggregate(2)(
      {(x : Int,y : Int) => x + y},
      {(a : Int,b : Int) => a * b}
    )
    println(b)
//    fold
//    def fold(zeroValue: T)(op: (T, T) ⇒ T): T
//    fold是aggregate的简化，将aggregate中的seqOp和combOp使用同一个函数op。
    println("flod=============")
    var flodRdd1 = sc.makeRDD(1 to 10,2)
    val c=flodRdd1.fold(1)(
             (x,y) => x + y
         )
    //##结果同上面使用aggregate的第一个例子一样，即：
    val d =flodRdd1.aggregate(1)(
                 {(x,y) => x + y},
                 {(a,b) => a + b}
             )
    println(c)
    println(d)
//    lookup
//    def lookup(key: K): Seq[V]
//    lookup用于(K,V)类型的RDD,指定K值，返回RDD中该K对应的所有V值。
    println("lookup=========")
    var lookupRdd1 = sc.makeRDD(Array(("A",0),("A",2),("B",1),("B",2),("C",1)))
    println(lookupRdd1.lookup("A"))
    println(lookupRdd1.lookup("B"))
    println(lookupRdd1.lookup("1"))
    val e=lookupRdd1.lookup("B")
    println(e.size)
    e.foreach(println)



  }
}

