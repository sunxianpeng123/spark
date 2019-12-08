package ScalaCollection
import collection.parallel._

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/8/2
  * \* Time: 16:54
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object collection_parallel {

  def main(args: Array[String]): Unit = {
    val list0 =List(1,2,3,4,5,6,7,8,9)
    println(list0.reduce(_+_))
    println(list0)
    println(list0.par)
    println(list0.par.reduce(_+_))
//    并行化集合 每个初始值100都会在每个线程内初始一次
//    多核 每次分配的线程都不一样 所以结果每次不一定相等
    println(list0.par.fold(100)(_+_))
    println(list0.par.fold(100)(_+_))
    println(list0.par.fold(100)(_+_))
    println(list0.par.fold(100)(_+_))
    println(list0.par.fold(100)(_+_))
    println("=====================")
    val arr = List(List(1,2,3), List(3,4,5), List(2), List(0))
    println(arr.aggregate(100)(_+_.sum,_+_))
    println(arr.par.aggregate(100)(_+_.sum,_+_))
    println(arr.par.aggregate(100)(_+_.sum,_+_))
    println(arr.par.aggregate(100)(_+_.sum,_+_))
    println(arr.par.aggregate(100)(_+_.sum,_+_))
    println(arr.par.aggregate(100)(_+_.sum,_+_))
    println(arr.par.aggregate(100)(_+_.sum,_+_))
    println(arr.par.aggregate(100)(_+_.sum,_+_))
    println(arr.par.aggregate(100)(_+_.sum,_+_))
  }
}

