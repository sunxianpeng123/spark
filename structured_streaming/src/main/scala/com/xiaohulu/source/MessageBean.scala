package com.xiaohulu.source

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/7/8
  * \* Time: 16:58
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
class MessageBean extends  Serializable {
//  .selectExpr("content", "from_id", "room_id", "timestamp","platform_id as plat")
  var platform_id :String = ""
  var room_id :String = ""

  var from_id :String =""
  var content :String = ""

  var timestamp:String = ""
  var date:String = ""

}
