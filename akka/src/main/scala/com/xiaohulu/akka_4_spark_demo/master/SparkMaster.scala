package com.xiaohulu.akka_4_spark_demo.master

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.xiaohulu.akka_4_spark_demo.protocol._

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/2
  * \* Time: 18:24
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
class SparkMaster extends  Actor{
  val workers = scala.collection.mutable.Map[String,WorkerInfo]()
  override def receive = {
    case  "start" => {
      println("master服务器启动...")
      self ! StartTimeOutWorker
    }
    case RegisterWorkerInfo(id,cpu,ram)=>{
//      接收到worker注册信息，将注册的worker装入hashmap中进行管理
      if (!workers.contains(id)){
//        创建 WorkerInfo 对象实例
        val workerInfo = new WorkerInfo(id,cpu,ram)
//        加入到workers 中
        workers += ( id -> workerInfo )
        println(s"now has workers num = ${workers.size}")
//        回复消息，返回注册成功信息
        sender() ! RegisteredWorkerInfo
      }
    }
    case  HeartBeat(id) =>{
//      更新对应的worker的心跳时间
      val workerinfo = workers.get(id)
      workerinfo.get.lastHeartBeat = System.currentTimeMillis()
      println(s"master 更新了worker  id =$id 的时间 ")
    }
    // 开启定时器，每隔一定时间检测是否有worker的心跳超时
    case StartTimeOutWorker => {
      import context.dispatcher // 使用调度器时候必须导入dispatcher
      import scala.concurrent.duration._
      //第一个参数：0 代表立即触发该定时器，不延时
      //第二个参数： 3000 每隔三秒执行一次
      //      第三个：发给自己
      //      SendHeartBeat发送的内容
      context.system.scheduler.schedule(0 millis, 9000 millis, self, RemoveTimeOutWorker)
    }
    case RemoveTimeOutWorker => {
      val workerInfos = workers.values
      val currentTime = System.currentTimeMillis()
      // 过滤心跳超时的worker
      workerInfos
        .filter(workerInfo => currentTime - workerInfo.lastHeartBeat > 6000)
        .foreach(workerInfo => workers.remove(workerInfo.id))
      println(s"-----还剩 ${workers.size} 存活的Worker-----")
    }

  }
}
object  SparkMaster{
  def main(args: Array[String]): Unit = {
    // 检验参数
//    if(args.length != 3) {
//      println("请输入参数：host port masterName")
//      sys.exit() // 退出程序
//    }
    val host ="127.0.0.1"// args(0)
    val port =10001// args(1)
    val masterName ="master-01"// args(2)
    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$host
         |akka.remote.netty.tcp.port=$port
            """.stripMargin)
    val actorSystem = ActorSystem("sparkMaster", config)
    val masterActorRef = actorSystem.actorOf(Props[SparkMaster],masterName)
    masterActorRef ! "start"

  }
}
