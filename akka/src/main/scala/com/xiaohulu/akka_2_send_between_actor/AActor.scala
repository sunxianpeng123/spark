package com.xiaohulu.akka_2_send_between_actor

import akka.actor.{Actor, ActorRef}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/30
  * \* Time: 13:36
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
//主动者
class AActor(actorRef: ActorRef) extends  Actor {
  val bActorRef :ActorRef = actorRef
  override def receive = {
    case "start" => {
      println("AActor 出招了，start ok ")
      self.tell("我打",self)//发送给自己
//      self ! "我打"
    }
    case  "我打" => {
      //给BActor发送消息，需要持有BActor的引用（即 BActorRef）
      println("AActor(黄飞鸿) 厉害 看我佛山无影脚")
      Thread.sleep(1000)
      bActorRef.tell("我打",self)//self指的是BActorRef发送给BActor
//      bActorRef ! "我打"
    }
  }
}
