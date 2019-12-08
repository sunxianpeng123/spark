package dao

import java.sql.{Connection, Date, PreparedStatement}

import org.apache.spark.sql.Row


class statisticsDao(statisticsDate:Date) extends Serializable{


  var conn: Connection = null
  //conn= dbConnect.getConnect()

  val inStatisticsDate=statisticsDate



  def insertAnchor_message_pre_minute_statistics(iterator: Iterator[Row]): Unit = {
    var ps: PreparedStatement = null
    val sql = "insert into anchor_message_pre_minute_statistics(platform_id,room_id ,source_link,message_send_date,message_count,statistics_date,update_date) values (?, ?,?,?,?,?,?)"
    try {
      val date=	new Date(System.currentTimeMillis());
      iterator.foreach(data => {
        ps = conn.prepareStatement(sql)
        ps.setInt(1, data.getInt(1))
        ps.setString(2, data.getString(2))
        ps.setString(3, data.getString(3))
        ps.setDate(4,data.getDate(4))
        ps.setInt(5, data.getInt(5))
        ps.setDate(6, inStatisticsDate)
        ps.setDate(7, date);
        ps.executeUpdate()
      }
      )
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (ps != null) {
        ps.close()
      }

    }
  }



  def insertAnchor_message_statistics(iterator: Iterator[Row]): Unit = {
    var ps: PreparedStatement = null
    val sql = "insert into anchor_message_statistics(platform_id,room_id ,source_link,from_id,message_count,statistics_date,update_date) values (?, ?,?,?,?,?,?)"
    try {
      val date=	new Date(System.currentTimeMillis());
      iterator.foreach(data => {
        ps = conn.prepareStatement(sql)
        ps.setInt(1, data.getInt(1))
        ps.setString(2, data.getString(2))
        ps.setString(3, data.getString(3))
        ps.setInt(4, data.getInt(5))
        ps.setInt(5, data.getInt(5))
        ps.setDate(6, inStatisticsDate)
        ps.setDate(7, date);
        ps.executeUpdate()
      }
      )
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (ps != null) {
        ps.close()
      }

    }
  }




  @transient
  def wordCount(iterator: Iterator[(String,Int)]): Unit = {
    var ps: PreparedStatement = null
    val sql = "insert into word_count(word, count,statistics_date,update_date) values (?, ?,?,?)"
    try {
      val date=	new Date(System.currentTimeMillis());
      iterator.foreach(data => {
        ps = conn.prepareStatement(sql)
        ps.setString(1,data._1)
        ps.setInt(2, data._2)
        ps.setDate(3,date)
        ps.setDate(4,date)
        ps.executeUpdate()
      }
      )
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (ps != null) {
        ps.close()
      }

    }
  }




  def closeDB(){

    conn.close()

  }


}



