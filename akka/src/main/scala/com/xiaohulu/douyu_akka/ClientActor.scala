package com.xiaohulu.douyu_akka

import java.io.{BufferedReader, InputStreamReader}
import java.net.{InetSocketAddress, Socket}

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import akka.io.Tcp
import com.xiaohulu.douyu_akka.protocol.DouYu
import com.typesafe.config.Config
/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/9/4
  * \* Time: 17:03
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
class ClientActor(host:String,port:Int) extends Actor {
  override def receive = {
    case "start" =>{
      println("-----开始-----")
      self ! DouYu
    }
    case DouYu =>{
      println("====")
      //1.建立TCP连接  1）IP地址，2) 端口号
      val sck=new Socket(host,port);
      //2、传输内容
      val content="type=loginreq/roomid=92000"
      val  bstream=content.getBytes("UTF-8")
      val os=sck.getOutputStream
      os.write(bstream)
      val info = "690".getBytes("utf-8")
      os.write(info)
      // 4.将字节输入流包装成字符缓冲输入流
      val br = new BufferedReader(new InputStreamReader(sck.getInputStream))
      var i = 0
      System.out.println(i)
//      while (br.readLine()!=null){
      while (true){
        val result = br.readLine()// 读取服务端返回的结果
        i += 1
        System.out.println(i)
        System.out.println(result)
      }

    }

  }
}
object ClientActorApp{
  def main(args: Array[String]): Unit = {
    val host = "openbarrage.douyutv.com"
    val port = 8601
    val clientActorSystem = ActorSystem("ClientActor")
    val clientActorRef = clientActorSystem.actorOf(Props(new ClientActor(host,port)))
    clientActorRef ! "start"

  }
}
