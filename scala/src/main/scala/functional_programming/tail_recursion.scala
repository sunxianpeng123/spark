package functional_programming

import scala.annotation.tailrec
import scala.util.control.TailCalls._

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/17
  * \* Time: 17:39
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object tail_recursion {
  /**
    * 尾递归
    * @param args
    */
  def main(args: Array[String]): Unit = {
    //@tailrec
    1 to 10  foreach(i=>{
      println(s"$i:\t${factorial(i)}")
    })
    //尾递归的trampoline优化
    println("=============================")
    for (i <- 1 to 5){
      val even=isEven((1 to i ).toList).result
      println(s"$i is even? $even")
    }

  }

  def factorial(i:Int):BigInt={
    @tailrec
    def fact(i:BigInt,accumulator:BigInt):BigInt=
      if (i==1) accumulator
      else fact(i-1,i*accumulator)
    fact(i,1)
  }


  def isEven(xs:List[Int]):TailRec[Boolean]={
    if(xs.isEmpty) done(true)
    else tailcall(isOdd(xs.tail))
  }
  def isOdd(xs:List[Int]):TailRec[Boolean]={
    if(xs.isEmpty) done(true)
    else tailcall(isEven(xs.tail))
  }

}

