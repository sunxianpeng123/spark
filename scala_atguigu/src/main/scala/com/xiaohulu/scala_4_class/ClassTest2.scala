package com.xiaohulu.scala_4_class

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/23
  * \* Time: 17:06
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object ClassTest2 {
  def main(args: Array[String]): Unit = {
//    按照A的主构造器实例化一个对象a，在过程中会取调用父类B的主构造器
    val a = new A
//  按照A的辅构造器实例化一个对象a2，在辅构造器中先调用A的主构造器时会仔调用B的主构造器
//    来保证A和B类的继承关系,这就是为什么在辅构造器中先调用类的主构造器的原因
    val a2 = new A("jack")
  }
}
class A extends B{
  def  this(name:String){
    this//调用A的主构造器
    println("this is constructor of A")
  }
}
class B {
  println("B····")
}
