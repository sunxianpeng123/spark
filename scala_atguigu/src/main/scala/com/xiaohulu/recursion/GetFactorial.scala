package com.xiaohulu.recursion

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2020/11/9
  * \* Time: 16:07
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object GetFactorial {
  def main(args: Array[String]): Unit = {
    val n = 10
    val fac = getFactorial(n)
    println(s"fac = $fac")
  }

  def getFactorial(n: Int): Int = {
    if (n <= 0) throw new Exception("The Number n is Little Than Zero")
    if (n == 1) 1
    else {
      getFactorial(n - 1) * n
    }
  }
}

