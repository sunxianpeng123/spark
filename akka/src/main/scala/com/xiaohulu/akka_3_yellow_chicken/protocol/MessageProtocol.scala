package com.xiaohulu.akka_3_yellow_chicken.protocol

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/30
  * \* Time: 19:37
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
//使用样例类构件协议
//客户端发给服务器协议（序列化的对象）
case class  ClientMessage(msg:String)//样例类默认实现了apply方法
//
case class  ServerMessage(msg:String)