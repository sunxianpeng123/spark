package com.xiaohulu.akka_3_yellow_chicken.client_actor

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.xiaohulu.akka_3_yellow_chicken.protocol.{ClientMessage, ServerMessage}

import scala.io.StdIn

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/30
  * \* Time: 18:56
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
class CustomerActor (serverHost: String, serverPort: Int) extends Actor{
  //  定义一个 ChickenServer 的 Ref
  var serverActorRefer: ActorSelection = _
  //  Actor 中有一个PreStart 方法，他会在actor运行前执行
  //  在akka的开发中，通常将初始化的工作放在PreStart方法中
  override def preStart(): Unit = {
    //  下面链接中的 Server 即 ChickenServer Actor中的 ActorSystem 的名字，
    // ChickenServer-01 为ChickenServer中ActorRef 的名字,必须一一对应
    this.serverActorRefer = context.actorSelection(s"akka.tcp://Server@${serverHost}:${serverPort}/user/ChickenServer-01")
    println("this.serverActorRefer=" + this.serverActorRefer)
  }
  override def receive: Receive = {
    case "start" => println("客户端启动了!!...")
    case mes:String => {
      serverActorRefer ! ClientMessage(mes)
    }
    case ServerMessage(mes) => {
      println("收到小黄鸡咨询老师(Server)：" + mes)
    }
  }
}
//主程序入口
object CustomerActorApp extends App{
  val (clientHost,clientPort,serverHost,serverPort) = ("127.0.0.1",9990,"127.0.0.1",9999)
  val config = ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$clientHost
       |akka.remote.netty.tcp.port=$clientPort
        """.stripMargin)
  val clientActorSystem = ActorSystem("client", config)
  //    //  创建 CustomerActor 的引用
  val clientActorRef: ActorRef = clientActorSystem.actorOf(Props(new CustomerActor(serverHost,serverPort)), "customerActor-01")
  clientActorRef ! "start"
  while (true) {
    println("请输入需要咨询的问题...")
    val mes = StdIn.readLine()
    clientActorRef ! mes
  }
}

