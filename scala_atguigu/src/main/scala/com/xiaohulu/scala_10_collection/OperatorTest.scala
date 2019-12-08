package com.xiaohulu.scala_10_collection

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/27
  * \* Time: 17:04
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object OperatorTest {
  def main(args: Array[String]): Unit = {
    // 1、如果想在变量名、类名等定义中使用语法关键字（保留字），可以配合反引号反引号
    val `val` = 42
    // 2、中置操作符：A 操作符 B 等同于 A.操作符(B)
    val n1 = 1
    val n2 = 2
    val r1 = n1 + n2
    val r2 = n1.+(n2)//看Int的源码即可说明t
    println("r1=" + r1 + " ，r2=" + r2)
//    中置操作符 自定义类重载操作符
    val monster = new Monster
    monster + 10
    println(monster.money)
    monster.+(10)
    println(monster.money)
// 3、后置操作符：A操作符 等同于 A.操作符，如果操作符定义的时候不带()则调用时不能加括号
    println(monster++)
    println(monster.++)
    println(monster.money)
// 4、前置操作符，+、-、！、~等操作符A等同于A.unary_操作符
    !monster
    println(monster.money)
    ~monster
    println(monster.money)



  }
}
class  Monster{
  var money :Int = 0
//  对操作符进行重载
  def  +(n:Int):Unit={
    this.money += n
  }
  def ++():Unit={
    this.money += 1
  }
  def unary_!():Unit={
    this.money = -this.money
  }
  def unary_~():Unit={
    this.money = -this.money
  }
}

