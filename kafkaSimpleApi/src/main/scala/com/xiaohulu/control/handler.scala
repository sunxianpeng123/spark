package com.xiaohulu.control

import java.net.URL

import com.xiaohulu.tools.PropertyUtil

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/8/17
  * \* Time: 10:34
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object handler {
  def propertyUtilInit():Unit={
    var files = System.getProperty("spark.files")
    println("000000000000files ===== "+files)
    if (files == null|| files.isEmpty()) {
      println("111111111111files ===== "+files)
      files = System.getProperty("spark.yarn.dist.files")
    }
    if (files == null|| files.isEmpty()) {
      println("22222222222222files ===== "+files)
      //      PropertyUtil.load("/work/spark_task/config/mongodb_douyin_tool.properties")
    } else {
      println("3333333333333333files ===== "+files)
      if (files.startsWith("file:")) {
        println("444444444444444444files ===== "+files)
        PropertyUtil.load(files.substring("file:".length))
        println( PropertyUtil.load(files.substring("file:".length)))
      } else if (files.startsWith("http")) {
        println("55555555555files ===== "+files)
        val url = new URL(files);
        PropertyUtil.load(url)
      }else{
        println("6666666666666files ===== "+files)
        PropertyUtil.load(files)
      }
    }
  }
}

