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
object GenericityTest3 {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3)
    println(getMidEle(list))
  }

  def getMidEle[A](list:List[A])={
    list(list.length/2)
  }
}

