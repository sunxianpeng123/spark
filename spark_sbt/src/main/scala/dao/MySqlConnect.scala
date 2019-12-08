package dao

import java.io.FileInputStream
import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}
import java.util.Properties

import tools.Helper


object mySqlConnect {
  var conn:Connection = null
  var properties:Properties=null
  var path:String=null


  def connect(): Connection ={
    try{
      if(conn==null){
        properties=new Properties()
        path =  Thread.currentThread().getContextClassLoader.getResource("db.properties").getPath
        properties.load(new FileInputStream(path))
        //println("###########MYSQLIP:"+properties.getProperty("mysqldbIp"));
        conn = DriverManager.getConnection("jdbc:mysql://"+properties.getProperty("mysqldbIp")+":3306/videoManage?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull",
          properties.getProperty("mysqldbUser"),  properties.getProperty("mysqldbPassword"))

      }
    }catch {
      case e: Exception => e.printStackTrace()
    }
    conn

  }
  def mysqlIp(): Unit ={
    if(conn!=null){
      println("###########MYSQLIP:"+properties.getProperty("mysqldbIp"));
    }
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
  //
//
  def updateMySql(id:Int,danmaku:String,danmaku_json:String):Unit={
    var stmt: PreparedStatement=null
    try {
      val sql ="update video set danmaku=?,danmaku_json = ? where id = ?"
      println("id="+id+",danmaku ="+danmaku+",danmaku_json="+danmaku_json)
      stmt = conn.prepareStatement(sql)
      stmt.setString(1,Helper.filterEmoji(danmaku))
      stmt.setString(2,Helper.filterEmoji((danmaku_json)))
      stmt.setInt(3,id)
      stmt.executeUpdate()
      //println("id="+id+"danmaku ="+Helper.filterEmoji(danmaku)+"danmaku_json="+Helper.filterEmoji((danmaku_json)))
    } catch {
      case e: Exception => {
        e.printStackTrace()
      }
    } finally {
      if(stmt!=null) {
        stmt.close()
      }
    }
  }

}

