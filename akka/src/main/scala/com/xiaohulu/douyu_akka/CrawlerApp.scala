package com.xiaohulu.douyu_akka

import akka.actor.{ActorSystem, Props}
import akka.io.Tcp
import com.typesafe.config.ConfigFactory

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/4
  * \* Time: 17:12
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object CrawlerApp {
  def main(args: Array[String]): Unit = {
    val host ="openbarrage.douyutv.com"// args(0)
    val port =8601// args(1)
    val name ="douyu-01"// args(2)
    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$host
         |akka.remote.netty.tcp.port=$port
            """.stripMargin)
    val crawlerActorSystem = ActorSystem("CrawlerApp", config)

    val crawlerActorRef = crawlerActorSystem.actorOf(Props(new ClientActor(host,port)),name)
    crawlerActorRef ! "start"

  }
}

