package dao

import java.io.FileInputStream
import java.sql.DriverManager.getConnection
import java.sql.{Connection, PreparedStatement, ResultSet}
import java.util
import java.util.Properties

import org.apache.spark.sql.Row
import tools.PropertyUtil


object HiveJDBC {
  var conn: Connection = null
  var properties:Properties=null
  var path:String=null
  def  getConnect()  ={

    try{
      if (conn == null) {
        Class.forName("org.apache.hive.jdbc.HiveDriver")
//        properties= new Properties()
//        val filePath = "db_streaming_test.properties"
//        properties.load(new FileInputStream(filePath))
        println("###########HIVEIP:"+PropertyUtil.getString("hive_ip"));
         conn = getConnection("jdbc:hive2://"+PropertyUtil.getString("hive_ip")+":"+PropertyUtil.getString("hive_port")+"/live_show", "", "")
      }

    }
    catch {
      case e: Exception => e.printStackTrace()
    }

  }

  def closeConnecet(): Unit ={

    conn.close()

  }



  def getTopDate(quary_sql:String) :java.util.List[Row] = {

    var stmt: PreparedStatement=null
    var rs:ResultSet=null
    val list :java.util.List[Row]=new util.ArrayList[Row]()
    try {
      stmt = conn.prepareStatement(quary_sql)
      stmt.execute(quary_sql)
      rs = stmt.executeQuery
      var gb : Row=null
      // platform_id,room_id,source_link ,word_info
      while (rs.next) {
        gb=  Row(rs.getInt("platform_id"),rs.getString("room_id"),rs.getString("source_link"),
          rs.getString("word_info")
        )
        list.add(gb)
      }

    }
    catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
    finally {
      if(rs!=null){
        rs.close()
      }
      if(stmt!=null) {
        stmt.close()
      }

    }
    println("get message Date end "+list.size())
    list

  }




}
