package com.xiaohulu.akka_4_spark_demo.protocol

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/2
  * \* Time: 18:46
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
class Protocol {

}
// worker注册信息 //MessageProtocol.scala
case class RegisterWorkerInfo(id: String, cpu: Int, ram: Int)

//这个是WorkerInfo, 保存在master的hashmap中的（该hashmap用来管理worker）
//将来这个workerinfo会扩展(比如增加worker最近一次的心跳时间)
class WorkerInfo(val id: String, val cpu: Int, val ram: Int){
  var lastHeartBeat:Long = System.currentTimeMillis()
}

//当worker注册成功，服务器返回一个 RegisteredWorkerInfo 对象
case object RegisteredWorkerInfo

//worker每个一定时间由定时器发给自己的一个消息
case object  SendHeartBeat
//worker每隔一定时间由定时器触发，而向master发送的协议消息
case  class  HeartBeat(id:String)

//master给自己发送一个检查超时的worker的信息
case  object StartTimeOutWorker
//master给自己发消息，检测worker,对于心跳超时的.
case  object  RemoveTimeOutWorker
