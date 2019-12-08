package com.xiaohulu.scala_10_collection

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/27
  * \* Time: 15:55
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
/**
  * 现在有一些商品，请使用Scala设计相关的样例类，完成商品捆绑打折出售。要求
  * 1、商品捆绑可以是单个商品，也可以是多个商品。
  * 2、打折时按照折扣x元进行设计.
  * 3、能够统计出所有捆绑商品打折后的最终价格
  */
object CollectionTest {
  abstract class Item // 项
  case class Book(description: String, price: Double) extends Item
  //Bundle 捆 ， discount: Double 折扣 ， item: Item* ：表示可变参数，可以传无数多个item
  case class Bundle(description: String, discount: Double, item: Item*) extends Item
  def main(args: Array[String]): Unit = {
    //给出案例表示有一捆数，单本漫画（40-10） +文学作品(两本书)（80+30-20） = 30 + 90 = 120.0
    val sale = Bundle("书籍", 10,  Book("漫画", 40), Bundle("文学作品", 20, Book("《阳关》", 80), Book("《围城》", 30)))
    val money = price(sale)
    println(money)//120.0
  }
  def price(it: Item): Double = {
    it match {
      case Book(_, p) => p
      //生成一个新的集合,_是将its中每个循环的元素传递到price中it中。递归操作,分析一个简单的流程
      case Bundle(_, disc, its @ _*) => its.map(price _).sum - disc
    }
  }
}
