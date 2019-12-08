package com.xiaohulu.scala_9_implicit

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/26
  * \* Time: 15:54
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object ImplicitAbout {
  //  底层会生成f1$1
//  将double类型转换成Int
//  隐式函数应当在作用于才能生效
  implicit def f1(d: Double): Int = {
    d.toInt
  }
}



