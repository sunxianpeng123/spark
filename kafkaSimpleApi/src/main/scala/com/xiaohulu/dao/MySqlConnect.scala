package com.xiaohulu.dao

import java.sql.{Connection, DriverManager, PreparedStatement, Timestamp}
import java.util.Properties

import com.xiaohulu.bean.MessageBean
import com.xiaohulu.tools.PropertyUtil
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka.OffsetRange

class MySqlConnect extends Serializable {
  var conn: Connection = null
  var properties: Properties = null
  var path: String = null


  def connect(): Connection = {
    try {
      if (conn == null) {
        //        properties=new Properties()
        //        path =  Thread.currentThread().getContextClassLoader.getResource("db_tycoonBaseInfoIncrease.properties").getPath
        ////        path=System.getProperty("user.dir") + "/db_tycoonBaseInfoIncrease.properties"//local
        //        properties.load(new FileInputStream(path))
        //        println("###########MYSQLIP:"+properties.getProperty("dbIpR"));
        //        conn = DriverManager.getConnection("jdbc:mysql://"+properties.getProperty("dbIpR")+":"+properties.getProperty("dbPortR")+"/live_platform_viewer?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&jdbcCompliantTruncation=false&useSSL=false",
        //          properties.getProperty("dbUserR"),  properties.getProperty("dbPasswordR"))
        conn = DriverManager.getConnection("jdbc:mysql://" +
          PropertyUtil.getString("dbIp") + ":" +
          PropertyUtil.getString("dbPort").toInt +
          "/live_platform_viewer?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&jdbcCompliantTruncation=false&useSSL=false",
          PropertyUtil.getString("dbUser"), PropertyUtil.getString("dbPassword"))
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
    conn
  }

  def connect(ip: String, port: String, user: String, password: String): Connection = {
    try {
      if (conn == null) {
        //        properties=new Properties()
        //        path =  Thread.currentThread().getContextClassLoader.getResource("db_tycoonBaseInfoIncrease.properties").getPath
        ////        path=System.getProperty("user.dir") + "/db_tycoonBaseInfoIncrease.properties"//local
        //        properties.load(new FileInputStream(path))
        //        println("###########MYSQLIP:"+properties.getProperty("dbIpR"));
        //        conn = DriverManager.getConnection("jdbc:mysql://"+properties.getProperty("dbIpR")+":"+properties.getProperty("dbPortR")+"/live_platform_viewer?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&jdbcCompliantTruncation=false&useSSL=false",
        //          properties.getProperty("dbUserR"),  properties.getProperty("dbPasswordR"))
        conn = DriverManager.getConnection("jdbc:mysql://" +
          ip + ":" + port.toInt + "/live_platform_viewer?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&jdbcCompliantTruncation=false&useSSL=false", user, password)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
    conn
  }

  def insertWordTfRoom(rdd: RDD[(String, String, String)], ip: String, port: String, user: String, password: String): Unit = {
    //    val sql = "insert into fans_figure.word_tf_room (platform_id,room_id,word_json,update_time) values (?,?,?,?)"
    //    val mySqlConnect=new MySqlConnect
    //    val  conn =mySqlConnect.connect(ip,port,user,password)
    //    val ps: PreparedStatement = conn.prepareStatement(sql)
    //    try{
    //      rdd.collect().foreach(tup=>{
    //        val updatetime=	 new Timestamp(System.currentTimeMillis())
    //        ps.setString(1,tup._1)
    //        ps.setString(2,tup._2)
    //        ps.setString(3,tup._3)
    //        println(tup._1+"____"+tup._2+"________"+tup._3)
    //        println("======================================================================================")
    //        ps.setTimestamp(4, updatetime)
    //        ps.addBatch()
    //      })
    //      ps.executeBatch()
    //    }catch {
    //      case e: Exception => e.printStackTrace()
    //    }finally {
    //      if (ps != null) {
    //        ps.close()
    //      }
    //      if(conn!=null){
    //        conn.close()
    //      }
    //    }


    rdd.foreachPartition(part => {
      val sql = "insert into fans_figure.word_tf_room (platform_id,room_id,word_json,update_time) values (?,?,?,?)"
      val mySqlConnect = new MySqlConnect
      val conn = mySqlConnect.connect(ip, port, user, password)
      val ps: PreparedStatement = conn.prepareStatement(sql)
      try {
        while (part.hasNext) {
          val tup = part.next()
          val updatetime = new Timestamp(System.currentTimeMillis())
          ps.setString(1, tup._1)
          ps.setString(2, tup._2)
          ps.setString(3, tup._3)
          //          println(tup._1+"____"+tup._2+"________"+tup._3)
          //          println("======================================================================================")
          ps.setTimestamp(4, updatetime)
          ps.addBatch()
        }
        ps.executeBatch()
      } catch {
        case e: Exception => e.printStackTrace()
      } finally {
        if (ps != null) {
          ps.close()
        }
        if (conn != null) {
          conn.close()
        }
      }
    })
  }

  def insertKafka_offset( tup:OffsetRange,consumerGroup:String): Unit = {
    val sql = "insert into test_sun.kafka_offset (topic , group_id , partition_id ,offset , update_time ) values (?,?,?,?,?)"
    val ps: PreparedStatement = conn.prepareStatement(sql)
    println("sql :: " + sql)
    try {
      val updatetime = new Timestamp(System.currentTimeMillis())
      ps.setString(1,tup.topic)
      ps.setString(2, consumerGroup)
      ps.setInt(3,tup.partition)
      ps.setLong(4, tup.untilOffset)
      ps.setTimestamp(5, updatetime)
      ps.addBatch()
      ps.executeBatch()
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
  }

  def insertMsgInfoKafkaOffset(msgInfoArray:Array[MessageBean], offsetRanges:Array[OffsetRange], consumerGroup:String): Unit = {
    val msgInfoSql = "insert into test_sun.msg_info (platform_id, room_id, from_id, content, update_time ) values (?,?,?,?,?)"
    val offsetSql = "replace into test_sun.kafka_offset (topic , group_id , partition_id ,offset , update_time ) values (?,?,?,?,?)"
    //设置事物为不自动提交
    conn.setAutoCommit(false)
    var ps: PreparedStatement = null
    try {
      println("===========插入msg info 表=============")
      ps = conn.prepareStatement(msgInfoSql)
      msgInfoArray.foreach(bean => {
        val updatetime = new Timestamp(System.currentTimeMillis())
        ps.setString(1, bean.platform_id)
        ps.setString(2, bean.room_id)
        ps.setString(3, bean.from_id)
        ps.setString(4, bean.content)
        ps.setTimestamp(5, updatetime)
        ps.addBatch()
      })
      ps.executeBatch()
      println("===========插入 kafka offset 表=============")
      ps = conn.prepareStatement(offsetSql)
      offsetRanges.foreach(offsetRange => {
        val updatetime = new Timestamp(System.currentTimeMillis())
        ps.setString(1, offsetRange.topic)
        ps.setString(2, consumerGroup)
        ps.setInt(3, offsetRange.partition)
        ps.setLong(4, offsetRange.untilOffset)
        ps.setTimestamp(5, updatetime)
        ps.addBatch()
      })
      ps.executeBatch()
      //事物提交
      conn.commit()
    } catch {
      //回滚
      case e: Exception => {
        try {
          conn.rollback()
        } catch {
          case e1: Exception => e1.printStackTrace()
        } finally {
          e.printStackTrace()
        }
      }
    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
  }

}

