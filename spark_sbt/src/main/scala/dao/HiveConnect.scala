package dao

import java.io.FileInputStream
import java.sql.{Connection, PreparedStatement, ResultSet}
import java.sql.DriverManager.getConnection
import java.util
import java.util.Properties
import org.apache.spark.sql.Row

object hiveConnect {
  var conn: Connection = null
  var properties:Properties=null
  var path:String=null

  def connect():Connection={
    try{
      if (conn == null) {
        //org.spark-project.hive
        //val driver="org.spark.hive.jdbc.HiveDriver"
        val driver="org.apache.hive.jdbc.HiveDriver"
        Class.forName(driver)
        properties= new Properties()
        path =  Thread.currentThread().getContextClassLoader.getResource("db.properties").getPath
        properties.load(new FileInputStream(path))

//        println("###########HIVEIP:"+properties.getProperty("hiveIp"));
        //println(properties.getProperty("hivePort"))
        conn = getConnection("jdbc:hive2://"+properties.getProperty("hiveIp")+":"+properties.getProperty("hivePort")+"/live_show", "", "")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
    conn
  }

  def hiveIp(): Unit ={
    if (conn!=null){
      println("###########HIVEIP:"+properties.getProperty("hiveIp"));
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
//按照分组，取出同一个group的数据，存入List[Row]
  def getGroupHiveData(groupSql:String):java.util.List[Row]={
    var stmt: PreparedStatement=null
    var rs:ResultSet=null
    val list :java.util.List[Row]=new util.ArrayList[Row]()

    try {
      stmt = conn.prepareStatement(groupSql)
      stmt.execute(groupSql)
      rs = stmt.executeQuery
      var gb : Row=null
      while (rs.next) {
        //println(rs.getString("datetime"))
        gb=  Row(rs.getInt("platform_id"),rs.getString("room_id"),rs.getString("datetime"),
          rs.getString("content"),rs.getString("date"),rs.getString("plat"),rs.getString("group")

        )

        list.add(gb)
      }
    } catch {
      case e: Exception => {
        e.printStackTrace()
      }
    } finally {
      if(rs!=null){
        rs.close()
      }
      if(stmt!=null) {
        stmt.close()
      }
    }
    list
  }
//
def getSegData(groupSql:String):Map[String,List[String]]={
  var tMap:Map[String,List[String]]=Map()
  var stmt: PreparedStatement=null
  var rs:ResultSet=null
  val list :java.util.List[Row]=new util.ArrayList[Row]()

  try {
    stmt = conn.prepareStatement(groupSql)
    stmt.execute(groupSql)
    rs = stmt.executeQuery
    var gb : Row=null
    while (rs.next) {

      val roomid=rs.getString("room_id")
      val content=rs.getString("content")
      if (tMap.contains(roomid)){
        tMap+=(roomid->(content::tMap(roomid)))
      }else {
        var tList: List[String] = List()
        tList = content :: tList
        tMap += (roomid -> tList)
      }
    }
  } catch {
    case e: Exception => {
      e.printStackTrace()
    }
  } finally {
    if(rs!=null){
      rs.close()
    }
    if(stmt!=null) {
      stmt.close()
    }
  }
  tMap
}

}

