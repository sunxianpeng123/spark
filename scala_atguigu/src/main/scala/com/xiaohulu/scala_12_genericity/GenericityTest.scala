package com.xiaohulu.scala_12_genericity

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/3
  * \* Time: 11:46
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object GenericityTest {
  def main(args: Array[String]): Unit = {
    val strMsg = new Message[String]("msg") {}

  }
}
// 在 Scala 定义泛型用[T]， s 为泛型的引用
abstract class  Message[T](s:T){
  def get:T = s
}
// 子类扩展的时候，约定了具体的类型
class StrMessage[String](msg: String) extends Message(msg)
class IntMessage[Int](msg: Int) extends Message(msg)

