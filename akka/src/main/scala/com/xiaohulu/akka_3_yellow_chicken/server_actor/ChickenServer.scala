package com.xiaohulu.akka_3_yellow_chicken.server_actor

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.xiaohulu.akka_3_yellow_chicken.protocol.{ClientMessage, ServerMessage}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/30
  * \* Time: 18:51
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
class ChickenServer extends  Actor{
  override def receive: Receive = {
    case "start" => println("服务器在9999端口上监听了....")
    case ClientMessage(mes) => {
      println("客户咨询问题是:" + mes)
      mes match {
        case "大数据学费是多少" => sender() ! ServerMessage("15000RMB")
        case "学校地址" => sender() ! ServerMessage("昌平区宏福大楼xxx路")
        case "可以学哪些技术" => sender() ! ServerMessage("JavaEE 大数据 Python")
        case _ => sender() ! ServerMessage("你说啥子~~")
      }
    }
  }
}
//主程序入口
object ChickenServerApp extends App {
  val host = "127.0.0.1" //服务端ip地址
  val port = 9999
  //创建config对象,指定协议类型，监听的ip和端口
  val config = ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$host
       |akka.remote.netty.tcp.port=$port
        """.stripMargin)
  //  创建ActorSystem
  private val serverActorSystem = ActorSystem("Server", config)
  private val chickenServer: ActorRef = serverActorSystem.actorOf(Props[ChickenServer], "ChickenServer-01")
  chickenServer ! "start"//给自己发送消息，server启动
}