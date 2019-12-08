package com.xiaohulu.kafka_tool

import java.text.SimpleDateFormat
import java.util.Date

import com.google.gson.{Gson, JsonParser}
import com.xiaohulu.bean.{DateBean, GiftBean, MessageBean}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, OffsetRange}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/7/8
  * \* Time: 15:19
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object KafkaStreamMap {
  var offsetRanges = Array.empty[OffsetRange]
//  def kafkaStreamToDStream(kafkaStream:InputDStream[(String,String)],chatTypeName:String):(DStream[Array[(Array[MessageBean],Array[GiftBean])]],Array[OffsetRange]) = {
    def kafkaStreamToDStream(kafkaStream:InputDStream[(String,String)],chatTypeName:String):DStream[Array[(Array[MessageBean],Array[GiftBean])]] = {
    val events = kafkaStream.transform { rdd =>
      offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      rdd
    }.map(line => {
      val jsonParse = new JsonParser()
      val gs =new Gson()
      val  sdf = new SimpleDateFormat("yyyyMMdd")
      var msg:scala.Array[(Array[MessageBean],Array[GiftBean])]=null
      var num = 0
      try {
//        println(line._2)
        val je=jsonParse.parse(line._2)
        var dbArr:Array[DateBean]=null
        if(je.isJsonArray){
          dbArr= gs.fromJson(je,classOf[Array[DateBean]])
        }else{
          val dbb= gs.fromJson(je,classOf[DateBean])
          dbArr= new Array[DateBean](1)
          dbArr(0)=dbb
        }
        msg = dbArr.map(m=>{
          var platID=""
          var roomID=""
          var msgArr:Array[MessageBean]=Array.empty
          var giftArr:Array[GiftBean]=Array.empty
          if(m!=null & m.item!=null & m.item.length>0){
            num += 1
            platID=m.sid
            roomID=m.roomid
            var giftBean :GiftBean= null
            var msgBean :MessageBean = null
            m.item.foreach(x=> {
              giftBean = new GiftBean
              msgBean = new MessageBean
              if(x!=null){
                if(x.typeName.equals(chatTypeName)){
                  msgBean.platform_id =platID
                  msgBean.room_id =roomID
                  msgBean.from_id = x.fromid
                  msgBean.timestamp =x.time
                  msgBean.content=x.content
                  val date  = sdf.format(new Date(x.time.toLong * 1000))
                  msgBean.date = date
//                  println(msgBean.toString)
                  msgArr :+= msgBean
                } else{
                  if(!x.price.trim.equals("")){
//                    println(line._2)
                    giftBean.platform_id = platID
                    giftBean.room_id =roomID
                    giftBean.from_id = x.fromid
                    giftBean.from_name = x.fromname
//                    giftBean.gift_name =x.giftname
                    giftBean.gift_type= x.gift_type
                    giftBean.price = x.price.toDouble
                    giftBean.count = x.count
                    giftBean.timestamp = x.time
                    giftBean.gift_name=x.giftname
                    giftBean.gift_id=x.giftid
                    val date  = sdf.format(new Date(x.time.toLong * 1000))
                    giftBean.date = date
                    giftArr :+= giftBean
                  }
                }
              }
            })
          }
          (msgArr,giftArr)
        })
      } catch {
        case e: Exception =>println(e.getMessage)
      }
      msg
    })
    events
  }

//  def kafkaStreamToDStream(kafkaStream:InputDStream[(String,String)],chatTypeName:String):DStream[((String,String),(Int,Double,String,Int,Int,Int))]={
//   val events = kafkaStream.map(line => {
//      val jsonParse = new JsonParser();
//      val gs =new Gson()
//      var msg:scala.Array[((String,String),(Int,Double,String,Int,Int,Int))]=null
//      try {
//                println(line._2)
//        val je=jsonParse.parse(line._2);
//        var dbArr:Array[DateBean]=null
//        if(je.isJsonArray){
//          dbArr= gs.fromJson(je,classOf[Array[DateBean]]);
//        }else{
//          val dbb= gs.fromJson(je,classOf[DateBean]);
//          dbArr= new Array[DateBean](1)
//          dbArr(0)=dbb
//        }
//        msg=dbArr.map(m=>{
//          var msgCount=0
//          var giftPrice=0d
//          var msg=""
//          var msgIdSet:Set[String]=Set()
//          var giftIdSet:Set[String]=Set()
//          var giftNum=0
//          var platID=""
//          var roomID=""
//          if(m!=null & m.item!=null &m.item.length>0){
//            platID=m.sid
//            roomID=m.roomid
//            m.item.foreach(x=> {
//              if(x!=null){
//                if(x.typeName.equals(chatTypeName)){
//                  msgCount=msgCount+1;
//                  msgIdSet=msgIdSet+x.fromid
//                  //msg= msg+x.time+"$$"+x.content.replaceAll("$","")+"$"
//                  //修改方便拆分字符串
//                  msg= msg+x.time+"$"+x.content.replaceAll("$","")+"$$"
//                } else{
//                  if(!x.price.trim.equals("")){
//                    giftPrice=giftPrice+(x.price.toDouble*x.count)
//                    giftIdSet=giftIdSet+x.fromid
//                    giftNum=giftNum+x.count
//                  }
//                }
//              }
//            })
//          }
//          ((platID,roomID),(msgCount ,giftPrice,msg,msgIdSet.size,giftIdSet.size,giftNum))
//        }
//        )}
//      catch {
//        case e: Exception =>println(e.getMessage)
//      }
//      msg
//    }).filter(e=> e!=null && e.length!=0).flatMap(e=>e).filter(x=>x._2._2!=0d || x._2._1!=0 || x._2._4!=0 || x._2._5!=0 || x._2._6!=0 )
//
//    events
//  }
}

