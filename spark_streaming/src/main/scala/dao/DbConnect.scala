package dao

import java.io.FileInputStream
import java.sql._
import java.util.Properties

import tools.PropertyUtil


class dbConnect {


  var conn: Connection = null
  var properties:Properties=null
  var path:String=null
  def init(): Unit ={
   getConnect()
 }


  def  getConnect() :Connection ={

    try{
        if (conn == null) {
//          properties= new Properties()
//          val filePath = "db_streaming_test.properties"
//          properties.load(new FileInputStream(filePath))
          conn = DriverManager.getConnection("jdbc:mysql://"+PropertyUtil.getString("dbIp")+
            ":"+PropertyUtil.getString("dbPort")+"/"+PropertyUtil.getString("dbDatebase")+
            "?autoReconnect=true&rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf8&useSSL=false",
          PropertyUtil.getString("dbUser"), PropertyUtil.getString("dbPassword"))
       //   conn = DriverManager.getConnection("jdbc:mysql://192.168.120.160:3306/live_platform?useUnicode=true&characterEncoding=UTF-8", "xiangjia", "xiangjia123")

        }

    }
    catch {
      case e: Exception => e.printStackTrace()
    }
     conn
    }

  def  closeConnect(){
    try{
      if(conn!=null){

        conn.close();
        conn=null;
      }
    }  catch {
      case e: Exception => e.printStackTrace()
    }
  }





  def insertRealTime_statistics(insertArray:scala.Array[((String,String),(Int,Double,String,Int,Int,Int))],strDate:String): Unit = {



    var ps: PreparedStatement = null
    val sql = "insert into real_time_statistics(platform_id,room_id ,msg_count,gift_price,statistics_date,msg_sender_num,gift_sender_num,gift_num) values (?,?,?,?,?,?,?,?)"
    try {
      ps = conn.prepareStatement(sql)
      insertArray.foreach(data => {
        ps.setInt(1, data._1._1.toInt)
        ps.setString(2, data._1._2)
        ps.setLong(3, data._2._1)
        ps.setDouble(4, data._2._2)
        ps.setString(5, strDate)
        ps.setString(6,data._2._4.toString)
        ps.setString(7,data._2._5.toString)
        ps.setString(8,data._2._6.toString)
        ps.addBatch()
      }

      )
      ps.executeBatch()
    }catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (ps != null) {
        ps.close()
      }
    }

  }
}

