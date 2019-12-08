package com.xiaohulu.sink

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.sql.{ForeachWriter, Row}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/9
  * \* Time: 11:24
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
class MySqlSink extends ForeachWriter[Row]{
  var conn:Connection= _
  var ps:PreparedStatement = _

  val sql="insert into rate(batchId,InputTimestamp,num) values(?,?,?)"
  override def open(partitionId: Long, version: Long): Boolean ={
    conn = getConnection()
    val sql = "insert into structured_stream_sink(batch_id,timestamp,num) values(?, ?, ?);"
    ps = conn.prepareStatement(sql)
    true
  }
  def getConnection():Connection= {
    var con :Connection = null
    try {
      Class.forName("com.mysql.jdbc.Driver")
      con = DriverManager.getConnection("jdbc:mysql://192.168.120.158:3306/test_sun?useUnicode=true&characterEncoding=UTF-8", "root", "1qaz@WSX3edc")
    } catch  {
      case  e:Exception =>System.out.println("-----------mysql get connection has exception , structured_stream_sink = "+ e.getMessage)
    }
    con
  }
  override def process(value: Row): Unit ={
    ps.setObject(1,value.get(0))
    ps.setObject(2,value.get(1))
    ps.setObject(3,value.get(2))
    val i: Int = ps.executeUpdate()
    println(i+" "+value.get(0)+" "+value.get(1)+" "+value.get(2))
  }
  override def close(errorOrNull: Throwable): Unit = {
    //关闭连接和释放资源
    if (conn != null) {
      conn.close()
    }
    if (ps != null) {
      ps.close()
    }
  }
}
