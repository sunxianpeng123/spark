package wordSeg

import java.text.SimpleDateFormat
import java.util.Date

import com.huaban.analysis.jieba.{JiebaSegmenter, WordDictionary}
import dao.hiveConnect
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConversions._
import scala.io.Source
/**
  * Created by Dell on 2017/7/13.
  */
object jieba_timeUsed {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val tMap=getSegStr()
    var sum_time:Long=0
    val jiebaSeg=new JiebaSegmenter()

    tMap.keys.foreach(roomid=>{
      println("roomid=",roomid)
      // 弹幕字符串
      val roomContengStr=tMap(roomid).mkString(",")
      //开始时间
      val dateFormat=new  SimpleDateFormat("yyyyMMddhhmmss")
      val startTime=dateFormat.format(new Date())
      println(startTime)
      //stopword
      var stopwords:Set[String]=Set()
      Source.fromFile("src/main/resources/stopWord.txt").getLines().foreach(line=>{
        stopwords+=line
      })
      val words=seg(jiebaSeg,roomContengStr,stopwords)
      //输出结果
      //println(words)
      //结束时间
      val endTime=dateFormat.format(new Date())
      println(endTime)
      println((endTime.toLong - startTime.toLong)+"mm")
      sum_time+=(endTime.toLong - startTime.toLong)
    })
    println(sum_time)

  }

  def seg(jiebaSeg:JiebaSegmenter,segStr: String, stopwords: Set[String]): List[String] = {
    var result: List[String] = List()
    val words =jiebaSeg.sentenceProcess(segStr)
    words.foreach(word => {
      if (!stopwords.contains(word)) {
        result = word :: result
      }
    })
  result
  }
  def getSegStr():Map[String,List[String]]= {
    //val sqlContext = SparkSession.builder().appName("read_hive_data").getOrCreate()
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
