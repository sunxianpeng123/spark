package com.xiaohulu.source

import java.text.SimpleDateFormat
import java.util.Date

import com.google.gson.{Gson, JsonParser}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/7/8
  * \* Time: 15:19
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object KafkaStreamMapUtil {
  def kafkaStreamToDStream(df:DataFrame, chatTypeName:String):RDD[Array[(Array[MessageBean],Array[GiftBean])]] = {
    df.rdd.map(row=>{
      val jsonParse = new JsonParser()
      val gs =new Gson()
      var msg:scala.Array[(Array[MessageBean],Array[GiftBean])]=null
      val  sdf = new SimpleDateFormat("yyyyMMdd")
      try {
        //        println(line._2)
        val je=jsonParse.parse(row.getAs[String]("value"))
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
                  msgArr :+= msgBean
                  //                  msgnum += 1
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

  }
}

