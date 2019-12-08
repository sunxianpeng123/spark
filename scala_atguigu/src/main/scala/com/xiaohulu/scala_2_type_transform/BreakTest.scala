package com.xiaohulu.scala_2_type_transform

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/22
  * \* Time: 17:16
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object BreakTest {
  def main(args: Array[String]): Unit = {
    import util.control.Breaks._
    //    while
    var n = 10
    breakable{
      while (n <= 20){
        n += 1
        println(s"n = $n")
        if ( n == 18 ){
          break()
        }
      }
    }
    // for
    breakable{
      for (i <- 10.to(20)){
        println(s"i  = $i ")
        if ( i == 18 ){
          break()
        }
      }
    }
  }
}

