package com.xiaohulu.scala_12_genericity

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/3
  * \* Time: 12:01
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 下界
  * \*/
object GenericityTest4 {
  def main(args: Array[String]): Unit = {
//    Earth为Animal的父类，调用父类方法
    biophony(Seq(new Earth, new Earth)).map(_.sound())
    println("======================")
    //    调用Animal方法
    biophony(Seq(new Animal, new Animal)).map(_.sound())
    println("======================")
    //    Bird为Animal的子类类，调用子类方法
    biophony(Seq(new Bird, new Bird)).map(_.sound())
    println("======================")

    val res = biophony(Seq(new Bird))
    val res2 = biophony(Seq(new Object))
    val res3 = biophony(Seq(new Moon))
    println("\nres2=" + res2)
    println("\nres3=" + res3)
  }
  def biophony[T >: Animal](things: Seq[T]) = things
}
  class Earth { //Earth 类
    def sound(){ //方法
      println("hello !")
    }
  }
  class Animal extends Earth{
    override def sound() ={ //重写了Earth的方法sound()
      println("animal sound")
    }
  }
  class Bird extends Animal{
    override def sound()={ //将Animal的方法重写
      println("bird sounds")
    }
  }
  class Moon {}


