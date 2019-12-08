package com.xiaohulu

import java.io.{DataInputStream, DataOutputStream, ObjectOutputStream}
import java.net.{InetAddress, InetSocketAddress, ServerSocket, Socket}
import java.nio.ByteBuffer
import java.nio.channels.{ServerSocketChannel, SocketChannel}

import akka.actor.{Actor, ActorSystem, Props}

import scalaj.http.Http

/**
 * Hello world!
 *
 */
object App  {
  def main(args: Array[String]): Unit = {

    val host = "openbarrage.douyutv.com"
    val port = 8601
    val str = "type=loginreq/roomid=58839"
    val heartBeat = "type@=keeplive/tick@=1439802131/"

    val path = s"http://www.$host:$port/$str"

    val a = "type=loginreq/roomid=968987/"

    println("本次获取的是："+ a)

    val socket = new Socket(host,port)
    try {
      val in = new DataInputStream(socket.getInputStream)
      val out = new DataOutputStream(socket.getOutputStream)
      val send = "type=loginreq/roomid=1300804"
//      while (true) {
        System.out.println("客户端：" + send);//输出键盘输出内容提示 ，也就是客户端向服务器端发送的消息
        out.writeUTF(send);//将客户端的信息传递给服务器

        val accpet = in.readUTF();// 读取来自服务器的信息
        System.out.println(accpet);//输出来自服务器的信息
//      }
    }catch {
      case e:Exception=>e.printStackTrace()
    }finally {
      socket.close()
    }
  }
}

