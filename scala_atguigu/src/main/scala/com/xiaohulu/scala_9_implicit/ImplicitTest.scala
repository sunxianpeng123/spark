package com.xiaohulu.scala_9_implicit

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/26
  * \* Time: 15:40
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object ImplicitTest {
  def main(args: Array[String]): Unit = {
    val  i :Int = 1.2
    println(i)
  }
  implicit def f1(d:Double):Int={
    d.toInt
//    val num :Int = 5.6//error，不可以在隐式函数中调用隐式函数
  }
}

