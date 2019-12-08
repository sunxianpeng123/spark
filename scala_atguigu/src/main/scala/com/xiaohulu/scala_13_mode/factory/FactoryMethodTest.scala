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
//object FactoryMethodTest {
//  def main(args: Array[String]): Unit = {
//    new BJOrderPizza
//  }
//}
//class BJOrderPizza extends OrderPizza {
//  override def createPizza(t: String): Pizza = {
//    var pizza: Pizza = null
//    if (t.equals("cheese")) {
//      pizza = new BJCheesePizza
//    } else if (t.equals("pepper")) {
//      pizza = new BJPepperPizza
//    }
//    pizza
//  }
//}
//class LDOrderPizza extends OrderPizza {
//  override def createPizza(t: String): Pizza = {
//    var pizza: Pizza = null
//    if (t.equals("cheese")) {
//      pizza = new LDCheesePizza
//    } else if (t.equals("pepper")) {
//      pizza = new LDPepperPizza
//    }
//    pizza
//  }
//}
//abstract class  OrderPizza{
//  //  对象的实例化推迟到子类
//  breakable{
//    var orderType: String = null
//    var pizza: Pizza = null
//    do{
//      println("请输入pizza的类型 ,使用工厂方法模式...")
//      orderType = StdIn.readLine()
//      //使用简单工厂模式来创建对象.
//      pizza = createPizza(orderType)
//      if (pizza == null) {
//        break()
//      }
//    }while (true)
//  }
//  //抽象方法
//  def createPizza(t: String): Pizza
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
//class BJCheesePizza extends Pizza {
//  override def prepare(): Unit = {
//    this.name = "北京的奶酪pizza"
//    println(this.name + " preparing..")
//  }
//}
//class BJPepperPizza  extends  Pizza {
//  override def prepare(): Unit = {
//    this.name = "北京的胡椒pizza"
//    println(this.name + " preparing..")
//  }
//}
//class LDCheesePizza extends Pizza {
//  override def prepare(): Unit = {
//    this.name = "伦敦的奶酪pizza"
//    println(this.name + " preparing..")
//  }
//}
//class LDPepperPizza  extends  Pizza {
//  override def prepare(): Unit = {
//    this.name = "伦敦的胡椒pizza"
//    println(this.name + " preparing..")
//  }
//}