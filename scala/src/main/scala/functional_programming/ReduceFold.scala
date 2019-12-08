package functional_programming

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/30
  * \* Time: 16:38
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object ReduceFold {
  def main(args: Array[String]): Unit = {
    println("reduce  ,fold============")
    val list =List(1,2,3,4,5,6)
    val r1 =list.reduce(_+_)
    val f1 =list.fold(10)(_*_)
    println(r1)
    println(f1)
    //
    val fold1 = list.fold(10) _
    println(fold1)
    val f2 =fold1(_*_)
    println(f2)
    println("optionReduce,fold 操作空集合==============")
    val f3 =List.empty[Int].fold(10)(_*_)
    println(f3)
    val r2 =List.empty[Int].reduceOption(_+_)
    println(r2)
  println("foldRight================")
    val f4 =(list.foldRight(List.empty[String])){
      (x,list)=>("["+x+"]")::list
    }
    println(f4)
    println("向左遍历和向右遍历reduce,fold================")
    val f5=list.foldLeft(10) (_*_)
    val f6=list.foldRight(10)(_*_)
    println(f5)
    println(f6)
    val r3 =list.reduceLeft(_*_)
    val r4 =list.reduceRight(_*_)
    println(r3)
    println(r4)

  }
}

