package com.xiaohulu.akka_4_spark_demo.worker

import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.xiaohulu.akka_4_spark_demo.protocol.{HeartBeat, RegisterWorkerInfo, RegisteredWorkerInfo, SendHeartBeat}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/2
  * \* Time: 18:33
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
class SparkWorker(masterHost:String,masterPort:Int) extends  Actor{
  //master的代理即引用ref
  var masterPorxy :ActorSelection = _
  val id = UUID.randomUUID().toString
  override def preStart(): Unit = {
    masterPorxy = context.actorSelection(s"akka.tcp://sparkMaster@$masterHost:$masterPort/user/master-01")
  }
  override def receive = {
    case "start" => {
      println("Worker 启动成功...")
      //    向 master 发送注册信息
      masterPorxy ! RegisterWorkerInfo(id,16,16*1024)
    }
    case RegisteredWorkerInfo => {
      println(s"workerid = $id  regeist success!")
      //      注册成功后，就定义一个定时器，每隔一定时间，发送SendHeartBeat给自己
      import context.dispatcher
      import scala.concurrent.duration._
      //第一个参数：0 代表立即触发该定时器，不延时
      //第二个参数： 3000 每隔三秒执行一次
      //第三个：发给自己
      //SendHeartBeat发送的内容
      context.system.scheduler.schedule(0 millis, 3000 millis, self, SendHeartBeat)
    }
    case  SendHeartBeat =>{
      println(s"work = $id  给master发送心跳")
      masterPorxy ! HeartBeat(id)
    }

  }
}
object SparkWorker{
  def main(args: Array[String]): Unit = {
//    if(args.length != 5) {
//      println("请输入参数：host port workerName masterHost masterPort")
//      sys.exit() // 退出程序
//    }
    val host = "127.0.0.1"//args(0)
    val port = 10002//args(1)
    val masterHost="127.0.0.1"//args(2)
    val masterPort=10001//args(3)
    val workerName = "worker-01"//args(4)
    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$host
         |akka.remote.netty.tcp.port=$port
            """.stripMargin)
    val actorSystem = ActorSystem("sparkWorker", config)
    val workerActorRef = actorSystem.actorOf(Props(new SparkWorker(masterHost,masterPort)), workerName)
    workerActorRef ! "start"
  }
}
