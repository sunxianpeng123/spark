package com.xiaohulu.scala_13_mode.singleton

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/19
  * \* Time: 19:09
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object HungrySingleTest {
  def main(args: Array[String]): Unit = {
    val singleTon = SingleTon.getInstance
    val singleTon2 = SingleTon.getInstance
    println(singleTon.hashCode() + " ~ " + singleTon2.hashCode())
    println(singleTon == singleTon2)
  }
}
//将SingleTon的构造方法私有化
class  SingleTon private (){
  println("~~~")
}
object SingleTon{
  private  val s:SingleTon = new SingleTon
  def getInstance={
    s
  }
}