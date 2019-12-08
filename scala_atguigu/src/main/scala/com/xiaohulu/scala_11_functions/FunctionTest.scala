package com.xiaohulu.scala_11_functions

import java.text.SimpleDateFormat
import java.util.Date

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/28
  * \* Time: 19:21
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object FunctionTest {
  def main(args: Array[String]): Unit = {
    val n  = 10
    println(factorial(n))
  }
  def factorial(n: Int): Int ={
    if (n == 1) n
    else factorial(n - 1) * n
  }
}

