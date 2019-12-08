package com.xiaohulu.scala_4_class

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/23
  * \* Time: 16:21
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object ClassTest {
  def main(args: Array[String]): Unit = {
  }
}
class Person {
  var age: Short = 90
  var name: String = _
  def this(n: String, a: Short) {
    this()
    this.name = n
    this.age =a
  }
}
