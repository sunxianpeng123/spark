package com.xiaohulu

import java.text.SimpleDateFormat
import java.util.Date


/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/25
  * \* Time: 11:38
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object Test {
  def main(args: Array[String]): Unit = {
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val gift_timestamp = 1569483217
    val updatetime = sdf.format(new Date(gift_timestamp *1000L))
    println(updatetime)


  }
}

