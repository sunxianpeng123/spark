package com.xiaohulu.scala_7_static

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/25
  * \* Time: 16:57
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object ApplyTest {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3)
    val pig = new Pig("小花")
//    使用apply方法来创建对象
    val pig2 = Pig("小黑猪")//触发def apply(pName: String): Pig = new Pig(pName)方法
    val pig3 = Pig()//触发def apply(): Pig = new Pig("匿名猪猪")
    println(s"pig2 = ${pig2.name}")
    println(s"pig3 = ${pig3.name}")
  }
}
//apply方法演示
class Pig(pName:String){
  var name:String = pName
}
object  Pig{
//  编写apply方法
  def apply(pName: String): Pig = new Pig(pName)
  def apply(): Pig = new Pig("匿名猪猪")
}
