package com.xiaohulu.scala_2_type_transform

import java.util.Scanner

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/22
  * \* Time: 11:59
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object compute {
  def main(args: Array[String]): Unit = {
    val age = 7
    val res = if (age>20){
      println("age is bigger than 20")
      "yes ok"
    }else{
      7
    }
    println( s"res = $res")


    val sumVal = 9
    val result = if (sumVal > 20){
      "结果大于20"
    }
    println( s"result = $result")//返回Unit,()

    val result2 =if (sumVal > 8){
      "结果大于20"
    }
    println( s"result2 = $result2")

  }
}

