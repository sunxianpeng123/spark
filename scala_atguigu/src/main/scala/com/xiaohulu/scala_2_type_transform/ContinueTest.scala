package com.xiaohulu.scala_2_type_transform

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/22
  * \* Time: 18:58
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object ContinueTest {
  def main(args: Array[String]): Unit = {
    val res = f3("a")
    println(res)
    val res2 = f4("b")
    println(res2)
  }

  def f3(s: String) = {
    if(s.length >= 3){
      s + "123"
    }else{
      3
    }
  }
  def f4(s: String): Any = {
    if(s.length >= 3){
      s + "123"
    }else{
      3
    }
  }
}

