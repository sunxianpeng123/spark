//package com.xiaohulu.scala_13_mode.factory
//
//import scala.io.StdIn
//import scala.util.control.Breaks.{break, breakable}
//
///**
//  * \* Created with IntelliJ IDEA.
//  * \* User: sunxianpeng
//  * \* Date: 2019/9/19
//  * \* Time: 15:03
//  * \* To change this template use File | Settings | File Templates.
//  * \* Description:
//  * \*/
//
//object SimpleFactoryTest {
//  def main(args: Array[String]): Unit = {
//    val simplePizzaFactory = new SimplePizzaFactory
//    var orderType : String = ""
//    var pizza :Pizza = null
//    breakable(
//      do {
//        println("请输入pizza的类型")
//        orderType = StdIn.readLine()
//        //使用简单工厂模式来创建对象.
//        pizza = simplePizzaFactory.createPizza(orderType)
//        if (pizza == null) {
//          break()
//        }
//      }while (true)
//    )
//  }
//}
//class SimplePizzaFactory {
//  def createPizza(t : String):Pizza={
//    var pizza:Pizza = null
//    if (t.equals("greek")) {
//      pizza = new GreekPizza
//    } else if (t.equals("pepper")) {
//      pizza = new PepperPizza
//    } else if (t.equals("cheese")) {
//      pizza = new CheesePizza
//    }
//    pizza
//  }
//}
//abstract class Pizza {
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
//class CheesePizza extends Pizza {
//  override def prepare(): Unit = {
//    this.name = "奶酪pizza"
//    println(this.name + " preparing..")
//  }
//}
//class GreekPizza  extends  Pizza {
//  override def prepare(): Unit = {
//    this.name = "希腊pizza"
//    println(this.name + " preparing..")
//  }
//}
//class PepperPizza  extends  Pizza {
//  override def prepare(): Unit = {
//    this.name = "胡椒pizza"
//    println(this.name + " preparing..")
//  }
//}