//package com.xiaohulu.scala_13_mode.factory
//
//import scala.io.StdIn
//import scala.util.control.Breaks._
///**
//  * \* Created with IntelliJ IDEA.
//  * \* User: sunxianpeng
//  * \* Date: 2019/9/19
//  * \* Time: 14:38
//  * \* To change this template use File | Settings | File Templates.
//  * \* Description:
//  * \*/
//object TraditionalMethodApp {
//  def main(args: Array[String]): Unit = {
//    /**传统方式*/
//    val orderPizza = new TraditionalOrderPizza
//    println("退出程序....")
//  }
//}
//class TraditionalOrderPizza {
//  var orderType : String = _
//  var pizza :Pizza1 = _
//  breakable(
//    do {
//      println("请输入pizza的类型")
//      orderType = StdIn.readLine()
//      if (orderType.equals("greek")) {
//        this.pizza = new GreekPizza1
//      } else if (orderType.equals("pepper")) {
//        this.pizza = new PepperPizza1
//      } else if (orderType.equals("cheese")) {
//        this.pizza = new CheesePizza1
//      } else {
//        break()
//      }
//    }while (true)
//  )
//}
//abstract class Pizza1 {
//  var name :String = _
//  //假定，每种pizza 的准备原材料不同，因此做成抽象的..
//  def prepare()
//  def cut():Unit={
//    println(this.name + " cutting ..")
//  }
//  def bake(): Unit = {
//    println(this.name + " baking ..")
//  }
//  def box(): Unit = {
//    println(this.name + " boxing ..")
//  }
//}
//class CheesePizza1 extends Pizza1 {
//  override def prepare(): Unit = {
//    this.name = "奶酪pizza"
//    println(this.name + " preparing..")
//  }
//}
//class GreekPizza1  extends  Pizza1 {
//  override def prepare(): Unit = {
//    this.name = "希腊pizza"
//    println(this.name + " preparing..")
//  }
//}
//class PepperPizza1  extends  Pizza1 {
//  override def prepare(): Unit = {
//    this.name = "胡椒pizza"
//    println(this.name + " preparing..")
//  }
//}