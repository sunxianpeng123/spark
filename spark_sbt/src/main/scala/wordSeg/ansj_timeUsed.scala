package wordSeg

import java.text.SimpleDateFormat
import java.util.{ArrayList, Date}

import dao.hiveConnect
import org.ansj.domain.Result
import org.ansj.library.DicLibrary
import org.ansj.recognition.impl.StopRecognition
import org.ansj.splitWord.analysis.DicAnalysis
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.types.{DataTypes, StructField}
import org.apache.spark.sql.{Row, SparkSession}
import org.nlpcn.commons.lang.tire.domain.Forest

import scala.collection.JavaConversions._
import scala.io.Source

/**
  * Created by Dell on 2017/7/11.
  */
object ansj_timeUsed {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val tMap=getSegStr()
    var sum_time:Long=0
    tMap.keys.foreach(roomid=>{
      println("roomid="+roomid)
      // 弹幕字符串
      val roomContentStr=tMap(roomid).mkString(",")
      //开始时间
      val dateFormat=new  SimpleDateFormat("yyyyMMddhhmmss")
      val startTime=new Date().getTime
      println(startTime)
      //stopword
      var filter =new  StopRecognition()
      Source.fromFile("src/main/resources/stopWord.txt").getLines().foreach(line=>
        filter.insertStopWords(line)
      )
      //println("wordLen"+ roomContentStr.length)
      val words:Result=seg(roomContentStr,filter)
      //输出结果
//      words.foreach(word=>{
//        println(word.toString.split("/")(0))
//      })
      //结束时间
      val endTime=new Date().getTime()
      println(endTime)
      println((endTime - startTime)+"mm")
      sum_time+=(endTime - startTime)
    })
    println(sum_time)

  }

  def seg(segStr:String,filter:StopRecognition):Result={
    //  segStr待分词字符串，customList自定义用户词典，跳过word从txt文件加载字典的步骤，直接从数据库中取词
    //分词
    var words=DicAnalysis.parse(segStr)
    words=words.recognition(filter)
    //words.foreach(println)
    words
  }

  def getSegStr():Map[String,List[String]]= {
    var tMap: Map[String, List[String]] = Map()
    hiveConnect.connect()
    hiveConnect.hiveIp()
    //SELECT date FROM live_show_message WHERE plat=2 and `group`=0
    val groupSql = "select platform_id,room_id,datetime,content,date,plat,group from live_show_message where date = 20170301 and plat = 2 and group = 0"
    //20170301
    tMap = hiveConnect.getSegData(groupSql)
    hiveConnect.closeConnect()
    tMap
  }
}
