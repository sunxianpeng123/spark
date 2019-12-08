package com.xiaohulu.scala_7_static

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/25
  * \* Time: 16:07
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object StaticTest {
  def main(args: Array[String]): Unit = {
    val scalaPerson = new ScalaPerson
    println(scalaPerson.name)
    println(ScalaPerson.sex)
  }
}

