package com.xiaohulu.scala_6_encapsulation

import com.xiaohulu.scala_4_class.B

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/24
  * \* Time: 16:05
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object EncapTest {
  def main(args: Array[String]): Unit = {
    var monster = new Monster {
      override var name: String = "牛魔王"
      override def cry(): Unit = {
        println("牛魔王哼哼叫唤..")
      }
    }
    monster.cry()
  }
}
abstract class Monster{
  var name : String
  def cry()
}







