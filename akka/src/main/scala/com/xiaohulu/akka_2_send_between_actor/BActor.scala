package com.xiaohulu.akka_2_send_between_actor

import akka.actor.Actor

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/30
  * \* Time: 13:36
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
//被动者
class BActor extends  Actor{
  override def receive = {
    case  "我打" => {
      //给BActor发送消息，需要持有BActor的引用（即 BActorRef）
      println("BActor(乔峰) 挺猛 看我降龙十八掌")
      Thread.sleep(1000)
//      通过sender()可以获取到发送消息的actor的ref
//      sender().tell("我打",sender)
      sender() ! "我打"
    }
  }
}
