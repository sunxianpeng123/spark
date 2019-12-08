//package com.xiaohulu.scala_13_mode.singleton
//
///**
//  * \* Created with IntelliJ IDEA.
//  * \* User: sunxianpeng
//  * \* Date: 2019/9/19
//  * \* Time: 17:47
//  * \* To change this template use File | Settings | File Templates.
//  * \* Description:
//  * \*/
//object LazySigleTest {
//  def main(args: Array[String]): Unit = {
//    val singleTon = SingleTon.getInstance
//    val singleTon2 = SingleTon.getInstance
//    println(singleTon.hashCode() + " " + singleTon2.hashCode())
//  }
//}
////将SingleTon的构造方法私有化,伴生类
//class SingleTon private() {}
////伴生对象
//object SingleTon{
//  private var s:SingleTon = null
//  def  getInstance = {
//    if (s == null){
//      s = new SingleTon
//    }
//    s
//  }
//}
