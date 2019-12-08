package ConcurrentProgramming.Actor
import scala.actors.Actor
/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/10/22
  * \* Time: 10:36
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
//混入Actor特质，然后实现act方法
//如同java中的Runnable接口一样
//各线程的run方法是并发执行的
//Actor中的act方法也是并发执行的
class ActorDemo extends Actor{
  /***
    * java中的并发主要是通过线程来实现，各线程采用共享资源的机制来实现程序的并发，这里面临竞争资源的问题，虽然采用锁机制可以避免竞争资源的问题，
    * 但会存在死锁问题，要开发一套健壮的并发应用程序具有一定的难度。而scala的并发模型相比于java它更简单，
    * 它采用消息传递而非资源共享来实现程序的并发，消息传递正是通过Actor来实现的。
    */
  //实现 act()方法
  def act(): Unit ={
    while (true){
      //receive从邮箱中获取一条消息
      //然后传递给它的参数
      //该参数是一个偏函数
        receive {
          case "actorDemo" => println("receive.....ActorDemo")
        }
    }
  }
}

object ActorDemo{
  def main(args: Array[String]): Unit = {
    val actor=new ActorDemo
    actor.start()
    actor!"actorDemo"

  }


}
