package com.xiaohulu.scala_12_genericity

import com.xiaohulu.scala_12_genericity.SeasonEm.SeasonEm

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/3
  * \* Time: 12:01
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object GenericityTest2 {
  def main(args: Array[String]): Unit = {

    val class1 = new EnglishClass[SeasonEm, String, String](SeasonEm.spring, "001班", "高级班")
    println(class1.classSeason + " " + class1.className + " " + class1.classType)

    val class2 = new EnglishClass[SeasonEm, String, Int](SeasonEm.spring, "002班", 1)
    println(class2.classSeason + " " + class2.className + " " + class2.classType)
  }
}
// Scala 枚举类型
object SeasonEm extends Enumeration {
  //自定义SeasonEm，是Value类型,这样才能使用
  type SeasonEm = Value
  val spring, summer, winter, autumn = Value
}
// 定义一个泛型类
class EnglishClass[A, B, C](val classSeason: A, val className: B, val classType: C)


