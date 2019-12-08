package com.xiaohulu.akka_2_send_between_actor

import akka.actor.{ActorRef, ActorSystem, Props}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/30
  * \* Time: 13:36
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object ActorGame {
  //  创建ActorSystem
  val  actorFactory = ActorSystem("actorFactory")
  //  先创建BActor的引用（代理）
  val bActorRef :ActorRef = actorFactory.actorOf(Props[BActor],"bActor")
  //  创建AActor的引用
  val aActorRef :ActorRef = actorFactory.actorOf(Props(new AActor(bActorRef)),"aActor")

  def main (args: Array[String] ): Unit = {
    //AActor 出招
    aActorRef ! "start"
  }
}