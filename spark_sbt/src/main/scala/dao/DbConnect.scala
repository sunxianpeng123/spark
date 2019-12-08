package dao

import java.io.FileInputStream
import java.sql._
import java.util.Properties

import bean.rankScheduleBean
import tools.Helper


object dbConnect {

//初始化
  var inStatisticsDate:Date =null
  var endStatisticsDate:Date =null
  var inPlatFromID:Int =0
  var conn: Connection = null
  var properties:Properties=null
  var path:String=null
  var tableName:String =null
  def init(date:Date ,platFromID:Int): Unit ={
    inStatisticsDate=date
    inPlatFromID=platFromID
    getConnect()
 }

  def init(startDate:Date ,endDate:Date,platFromID:Int,table:String): Unit ={

    inStatisticsDate=startDate
    endStatisticsDate=endDate
    inPlatFromID=platFromID
    tableName =table
    getConnect()
  }


  //建立连接
  def  getConnect() :Connection ={

    try{
        if (conn == null) {
          properties= new Properties()
          path =  Thread.currentThread().getContextClassLoader.getResource("db.properties").getPath
          properties.load(new FileInputStream(path))
          println("###########DBIP:"+properties.getProperty("dbIp"));
          conn = DriverManager.getConnection("jdbc:mysql://"+properties.getProperty("dbIp")+":3306/live_platform?autoReconnect=true&useUnicode=true&characterEncoding=utf8", properties.getProperty("dbUser"),  properties.getProperty("dbPassword"))
       //   conn = DriverManager.getConnection("jdbc:mysql://192.168.120.160:3306/live_platform?useUnicode=true&characterEncoding=UTF-8", "xiangjia", "xiangjia123")

        }

    }
    catch {
      case e: Exception => e.printStackTrace()
    }
     conn
    }

//关闭连接
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


//删除表tableName中statistics_start_date，statistics_end_date，platform_id
  def delTopRankList(): Unit ={
    var ps: PreparedStatement = null//JDBC驱动的最佳化是基于使用的是什么功能. 选择PreparedStatement还是Statement取决于你要怎么使用它们. 对于只执行一次的SQL语句选
    // 择Statement是最好的. 相反,如果SQL语句被多次执行选用PreparedStatement是最好的.PreparedStatement的第一次执行消耗是很高的. 它的性能体现在后面的重复执行.
    val sql = "delete from "+tableName+"  where  statistics_start_date =? and statistics_end_date=? and platform_id =?"
    try{
      ps = conn.prepareStatement(sql)
      ps.setDate(1,inStatisticsDate)
      ps.setDate(2,endStatisticsDate)
      ps.setInt(3,inPlatFromID)
      ps.execute()
    }  catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (ps != null) {
        ps.close()
      }

    }

  }

  //从rank_config表中取出isRunAble =1的id,key_word,weight_proportion放在list中
  def getRankSchedule(): java.util.List[rankScheduleBean] = {
    var ps: PreparedStatement = null//JDBC驱动的最佳化是基于使用的是什么功能. 选择PreparedStatement还是Statement取决于你要怎么使用它们. 对于只执行一次的SQL语句选
    // 择Statement是最好的. 相反,如果SQL语句被多次执行选用PreparedStatement是最好的.PreparedStatement的第一次执行消耗是很高的. 它的性能体现在后面的重复执行.
    var rs:ResultSet  = null;
    val rsList = new java.util.ArrayList[rankScheduleBean]
    var rsb:rankScheduleBean=null

    val sql = "select id,key_word,weight_proportion from rank_config where isRunAble =1 ";
    try {
      ps = conn.prepareStatement(sql)
      rs=ps.executeQuery()//JDBC中Statement 接口提供了三种执行 SQL 语句的方法：executeQuery、executeUpdate、execute

      while(rs.next()) {
        rsb = new rankScheduleBean
        rsb.id=rs.getInt(1)
        rsb.key_word= rs.getString(2)
        rsb.weight_proportion=rs.getString(3)
        rsList.add(rsb)
      }
    }
    catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (ps != null) {
        ps.close()
      }
      if(rs!=null){

        rs.close()
      }}
    rsList
    }



//向tableName插入platform_id,room_id,source_link, score,ref_rank_config_id,statistics_start_date,statistics_end_date,update_date
  def insertRankList(iterator: Iterator[(Int,String,String,Int,Int)]): Unit ={
    var ps: PreparedStatement = null
    val sql = "insert into "+tableName+" (platform_id,room_id,source_link, score,  ref_rank_config_id,statistics_start_date,statistics_end_date,update_date) values (?, ?,?,?,?,?,? ,?)"
    try{
      val date=	 new Timestamp(System.currentTimeMillis())
      ps = conn.prepareStatement(sql)
      iterator.foreach(data => {
        ps.setInt(1,data._1)
        ps.setString(2, data._2)
        ps.setString(3, Helper.filterEmoji(data._3))
        ps.setInt(4,data._4)
        ps.setInt(5, data._5)
        ps.setDate(6, inStatisticsDate)
        ps.setDate(7, endStatisticsDate)
        ps.setTimestamp(8, date)
        ps.addBatch()//addBatch()把若干sql语句装载到一起，然后一次送到数据库执行，执行需要很短的时间

      }
      )
      ps.executeBatch()////批量执行预定义SQL
    }
    catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (ps != null) {
        ps.close()
      }

    }
  }

}

