package com.xiaohulu.dao
import java.util.Properties

import com.xiaohulu.tools.PropertyUtil
import org.apache.spark.sql.{DataFrame, SparkSession}
/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/24
  * \* Time: 15:54
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
class SparkJdbcWriteDao {
  private var path = ""
  var port = ""
  var ip = ""
  var sparkProperties = new Properties()
  def init(): Unit = {
    try {
      //k8s
      sparkProperties.put("driver", "com.mysql.jdbc.Driver")
      sparkProperties.put("user", PropertyUtil.getString("dbUser"))
      sparkProperties.put("password", PropertyUtil.getString("dbPassword"))
      sparkProperties.put("batchsize", 5000.toString)
      //      sparkProperties.put("fetchsize", "3")
      port = PropertyUtil.getString("dbPort")
      ip = PropertyUtil.getString("dbIp")
      println(s"================================#################dbIp  $ip===========================================================")
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def writeToDouyinAnchor(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false", "short_video_platform.anchor_base_info_today_tmp", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def writeToDouyinVideo(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false", "short_video_platform.video_base_info_today_tmp", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
  def writeToDouyinMusic(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false", "short_video_platform.music_base_info_today_tmp", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
  //day
  def writeTo_video_base_info_newly_day(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false",
        "short_video_platform.video_base_info_newly_day", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
  def writeTo_anchor_base_info_newly_day(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false",
        "short_video_platform.anchor_base_info_newly_day", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
  def writeTo_music_base_info_newly_day(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false",
        "short_video_platform.music_base_info_newly_day", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
  //week
  def writeTo_video_base_info_newly_week(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false",
        "short_video_platform.video_base_info_newly_week", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
  def writeTo_anchor_base_info_newly_week(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false",
        "short_video_platform.anchor_base_info_newly_week", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
  def writeTo_music_base_info_newly_week(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false",
        "short_video_platform.music_base_info_newly_week", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  //month
  def writeTo_video_base_info_newly_month(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false",
        "short_video_platform.video_base_info_newly_month", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
  def writeTo_anchor_base_info_newly_month(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false",
        "short_video_platform.anchor_base_info_newly_month", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
  def writeTo_music_base_info_newly_month(data:DataFrame,sqlContext: SparkSession): Unit = {
    try {
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false",
        "short_video_platform.music_base_info_newly_month", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }


  def writeTo_kafka_offsetDF(data:DataFrame,sqlContext: SparkSession): Unit = {
    try{
      println("*********************2222222222222222222222222")
      data.write.mode("append").jdbc("jdbc:mysql://" + ip + ":" + port+"?autoReconnect=true&rewriteBatchedStatements=true&jdbcCompliantTruncation=false&useUnicode=true&useSSL=false",
        "test_sun.kafka_offset", sparkProperties)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}