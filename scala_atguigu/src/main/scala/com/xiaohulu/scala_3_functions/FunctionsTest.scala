package com.xiaohulu.scala_3_functions

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/22
  * \* Time: 19:56
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object FunctionsTest {
  def main(args: Array[String]): Unit = {
    val person = new Person
    println(person.height)
  }
}
class Person {
  var age : Int = 10
  var sal = 8090.9//给属性赋初值，省略类型，会自动推导
  var Name  = null// Name 是什么类型? 
  var address : String = null // address 是什么类型？ 
  var height :Int = _
}





