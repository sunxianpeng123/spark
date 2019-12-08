package com.xiaohulu.akka_4_spark_demo.worker

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.xiaohulu.akka_4_spark_demo.protocol.{RegisterWorkerInfo, RegisteredWorkerInfo}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/2
  * \* Time: 18:33
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
class SparkWorker2(masterHost:String,masterPort:Int) extends  Actor{
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
    }
  }
}
object SparkWorker2{
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = 10003
    val masterURL = "akka.tcp://sparkMaster@127.0.0.1:10001/user/master-01"
    val masterHost="127.0.0.1"
    val masterPort=10001
    val workerName = "worker-02"
    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=127.0.0.1
         |akka.remote.netty.tcp.port=10003
            """.stripMargin)
    val actorSystem = ActorSystem("sparkWorker", config)
    val workerActorRef = actorSystem.actorOf(Props(new SparkWorker(masterHost,masterPort)), workerName)
    workerActorRef ! "start"
  }
}
