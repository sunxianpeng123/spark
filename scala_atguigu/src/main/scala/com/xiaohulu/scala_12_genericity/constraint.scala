package com.xiaohulu.scala_12_genericity

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/3
  * \* Time: 13:23
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 上界
  * \*/
object constraint {
  def main(args: Array[String]): Unit = {
    //常规方式
    /*
    val compareInt = new CompareInt(-10, 2)
    println("res1=" + compareInt.greater)
    val compareFloat = new CompareFloat(-10.0f, -20.0f)
    println("res2=" + compareFloat.greater)*/
    /*val compareComm1 = new CompareComm(20, 30)
    println(compareComm1.greater)*/
    val compareComm2 = new CompareComm(Integer.valueOf(20), Integer.valueOf(30))
    println(compareComm2.greater)
    val compareComm3 =
      new CompareComm(java.lang.Float.valueOf(20.1f), java.lang.Float.valueOf(30.1f))
    println(compareComm3.greater)
    val compareComm4 = new CompareComm[java.lang.Float](201.9f, 30.1f)
    println(compareComm4.greater)
  }
}

/*class CompareInt(n1: Int, n2: Int) {
  def greater = if(n1 > n2) n1 else n2
}
class CompareFloat(n1: Float, n2: Float) {
  def greater = if(n1 > n2) n1 else n2
}*/
class CompareComm[T <: Comparable[T]](obj1: T, obj2: T) {
  def greater = if(obj1.compareTo(obj2) > 0) obj1 else obj2
}