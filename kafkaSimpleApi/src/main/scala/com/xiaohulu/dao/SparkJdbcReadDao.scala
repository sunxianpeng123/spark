package com.xiaohulu.dao

import java.util.Properties

import com.xiaohulu.tools.PropertyUtil
import org.apache.spark.sql.{Column, DataFrame, SparkSession}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/24
  * \* Time: 15:54
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
class SparkJdbcReadDao {
  private var path = ""
  private var port = ""
  private var ip = ""
  private var sparkProperties = new Properties()
  private val partictionNum = 100

  def init(): Unit = {
    try {
      sparkProperties.put("driver", "com.mysql.jdbc.Driver")
      sparkProperties.put("user", PropertyUtil.getString("dbUser"))
      sparkProperties.put("password", PropertyUtil.getString("dbPassword"))
      sparkProperties.put("batchsize", 5000.toString)
      //      sparkProperties.put("fetchsize", "3")
      port = PropertyUtil.getString("dbPort")
      ip = PropertyUtil.getString("dbIp")
      println(s"========================#################dbIp  $ip============================")
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

//  def getMusic_base_info(platform_id:Int, sqlContext: SparkSession): DataFrame = {
//    val sql = s"(select * from  short_video_platform.music_base_info where platform_id =$platform_id ) anchor_base_info_tb "
//    var res: DataFrame = null
//    try {
//      println(sql)
//      res = sqlContext.read.jdbc(
//        "jdbc:mysql://" + ip + ":" + port+"?useSSL=False", sql, sparkProperties)// 注意括号和表别名，必须得有，这里可以过滤数据
//        .repartition(new Column("music_owner_id"))
//    } catch {
//      case e: Exception => e.printStackTrace()
//    }
//    res
//  }

//author
//  def getAuthor_base_info(platform_id:Int,sqlContext: SparkSession): DataFrame = {
//    val predicates =
//      0.until(10).toArray.map {
//        case (date) => s"room_id%10 =$date and platform_id =$platform_id "//(10,218749,4485518)
//      }
//    var res: DataFrame = null
//    try {
//      res = sqlContext.read.jdbc(
//        "jdbc:mysql://" + ip + ":" + port+"?useSSL=False", "short_video_platform.anchor_base_info",predicates,sparkProperties) // 注意括号和表别名，必须得有，这里可以过滤数据
//        .repartition(new Column("room_id"))
//    } catch {
//      case e: Exception => e.printStackTrace()
//    }
//    res
//  }
    def get_kafka_offsetDF( sqlContext: SparkSession): DataFrame = {
      val sql = s"(select * from  test_sun.kafka_offset  ) kafka_offset_table "
      var res: DataFrame = null
      try {
        println(sql)
        res = sqlContext.read.jdbc(
          "jdbc:mysql://" + ip + ":" + port+"?useSSL=False", sql, sparkProperties)// 注意括号和表别名，必须得有，这里可以过滤数据
      } catch {
        case e: Exception => e.printStackTrace()
      }
      res
    }



  //music
//  def getMusic_base_info(platform_id:Int,sqlContext: SparkSession): DataFrame = {
//    val predicates =
//      0.until(10).toArray.map {
//        case (date) => s"music_owner_id%10 =$date and platform_id =$platform_id "//(10,218749,4485518)
//      }
//    var res: DataFrame = null
//    try {
//      res = sqlContext.read.jdbc(
//        "jdbc:mysql://" + ip + ":" + port+"?useSSL=False", "short_video_platform.music_base_info",predicates,sparkProperties) // 注意括号和表别名，必须得有，这里可以过滤数据
//        .repartition(new Column("music_owner_id"))
//    } catch {
//      case e: Exception => e.printStackTrace()
//    }
//    res
//  }
    def getMusic_base_info(platform_id:Int, sqlContext: SparkSession): DataFrame = {
      val sql = s"(select * from  short_video_platform.music_base_info where platform_id =$platform_id ) music_base_info_tb "
      var res: DataFrame = null
      try {
        println(sql)
        res = sqlContext.read.jdbc(
          "jdbc:mysql://" + ip + ":" + port+"?useSSL=False&autoReconnect=true", sql, sparkProperties)// 注意括号和表别名，必须得有，这里可以过滤数据
          .repartition(new Column("music_owner_id"))
      } catch {
        case e: Exception => e.printStackTrace()
      }
      res
    }





  //video
//  def getVideo_base_info(platform_id:Int,sqlContext: SparkSession): DataFrame = {
//    val predicates =
//      0.until(10).toArray.map {
//        case (date) => s"video_owner_id%10 =$date and platform_id =$platform_id "//(10,218749,4485518)
//      }
//    var res: DataFrame = null
//    try {
//      res = sqlContext.read.jdbc(
//        "jdbc:mysql://" + ip + ":" + port+"?useSSL=False", "short_video_platform.video_base_info",predicates,sparkProperties) // 注意括号和表别名，必须得有，这里可以过滤数据
//        .repartition(new Column("video_owner_id"))
//    } catch {
//      case e: Exception => e.printStackTrace()
//    }
//    res
//  }
  def getVideo_base_info(platform_id:Int, sqlContext: SparkSession): DataFrame = {
    val sql = s"(select * from  short_video_platform.video_base_info where platform_id =$platform_id ) video_base_info_tb "
    var res: DataFrame = null
    try {
      println(sql)
      res = sqlContext.read.jdbc(
        "jdbc:mysql://" + ip + ":" + port+"?useSSL=False&autoReconnect=true", sql, sparkProperties)// 注意括号和表别名，必须得有，这里可以过滤数据
        .repartition(new Column("video_owner_id"))
    } catch {
      case e: Exception => e.printStackTrace()
    }
    res
  }

  def getVideo_base_info(dateArray:IndexedSeq[String],platform_id:Int,sqlContext:SparkSession):DataFrame={

    val predicates =
      dateArray.toArray.map {
        case (date) => s" video_create_time >= '${date.split("_")(0)}' and video_create_time <'${date.split("_")(1)}' and platform_id =$platform_id "
      }
    var res:DataFrame=null
    try{
      res= sqlContext.read.jdbc(
        "jdbc:mysql://"+ip+":"+port+"?useSSL=False&autoReconnect=true","short_video_platform.video_base_info  FORCE index(video_create_time)" ,predicates,sparkProperties)
        .repartition(new Column("video_owner_id"))
    }catch {
      case e:Exception=>e.printStackTrace()
    }
    res
  }
}