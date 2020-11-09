package com.xiaohulu.recursion

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2020/11/9
  * \* Time: 15:59
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object ReverseString {
  def main(args: Array[String]): Unit = {

    val str = "HelloWorld"
    val reversedStr = getReverseStr(str)
    println(s"reversedStr = $reversedStr")

  }

  def getReverseStr(str: String): String = {
    if (str == null) throw new Exception("The String is NUll!")
    else {
      if (str.length == 1) str else getReverseStr(str.tail) + str.head
    }
  }

}
