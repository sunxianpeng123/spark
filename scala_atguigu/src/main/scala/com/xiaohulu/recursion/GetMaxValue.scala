package com.xiaohulu.recursion

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2020/11/9
  * \* Time: 15:51
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object GetMaxValue {
  def main(args: Array[String]): Unit = {
    val tList = List(1, 6, 3, 9, 33, 44, 82, 1)

    val max = getMax(tList)

    println(s"max = $max")

  }

  def getMax(l: List[Int]): Int = {
    if (l.isEmpty) throw new Exception("The Length of List is Wrong!!")
    else if (l.length == 1) l.head
    else {
      if (l.head > getMax(l.tail)) l.head
      else getMax(l.tail)
    }
  }

}

