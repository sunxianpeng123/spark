package com.xiaohulu.akka_1_send_self

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/30
  * \* Time: 12:12
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 编写一个Actor, 比如SayHelloActor ，SayHelloActor 可以给自己发送消息
  * \*/
//继承Actor后，就是一个Actor，核心方法receive需要重写
class SayHelloActor extends  Actor{
  //1、receive方法会被该Actor的MailBox（实现了Runnable接口）调用
  //2、当Actor接收大MailBox接收到消息，就会调用receive
  //3、type Receive = PartialFunction[Any, Unit] ,是一个偏函数
  override def receive : Receive = {
    case "hello" => println("收到hello， 回应 hello too !")
    case "ok" => println("收到 ok， 回应 ok too !")
    case "exit" => {
      println("接收到exit指令，退出系统")
      context.stop(self)//停止actorref
      context.system.terminate()//退出actorsystem
    }
    case  _ =>println("匹配不到")
  }
}
object  SayHelloActorDemo{
  //  1 先创建一个ActorSystem，专门用于创建Actor
  private val  actorFactory = ActorSystem("actorFactory")
  //  2 创建Actor的同时，返回 Actor 的 ActorRef
  //  Props[SayHelloActor]创建了一个SayHelloActor实例，使用反射创建
  //  sayHelloActor Actor的名字
  //  sayHelloActorRef:ActorRef 就是 Props[SayHelloActor] 的 ActorRef
  //  创建的SayHelloActor实例，被ActorSystem接管，
  //  actorFactory.actorOf(Props[SayHelloActor],"sayHelloActor")
  private val sayHelloActorRef:ActorRef =  actorFactory.actorOf(Props(new SayHelloActor),"sayHelloActor")
  def main(args: Array[String]): Unit = {

    //    给SayHelloActor 发送消息（邮箱）
    //    下面两例子均发送给自己
    sayHelloActorRef ! "hello"
    sayHelloActorRef.tell("ok",sayHelloActorRef)
    sayHelloActorRef ! "no msg"
    sayHelloActorRef ! "exit"
  }
}
