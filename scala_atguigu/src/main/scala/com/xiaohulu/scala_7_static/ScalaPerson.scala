package com.xiaohulu.scala_7_static

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/25
  * \* Time: 16:07
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/

//伴生类
//class  ScalaPerson 编译后生成ScalaPerson 类 ScalaPerson.class
//object ScalaPerson 编译后生成ScalaPerson$ 类 ScalaPerson$.class
class  ScalaPerson{
  var name:String = _
}
//伴生对象
object ScalaPerson {
  var sex:Boolean = true
  def sayHi():Unit={
    println("object ScalaPerson")
  }
}