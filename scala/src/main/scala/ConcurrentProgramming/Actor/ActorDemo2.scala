package ConcurrentProgramming.Actor
import  scala.actors.Actor._
/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/10/22
  * \* Time: 11:04
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object ActorDemo2 {
  def main(args: Array[String]): Unit = {

    //通过工具方法actor直接创建Actor对象
    val methodActor = actor {
      for (i <- 1 to 5)
        println("That is the question.")
      Thread.sleep(1000)

    }

  }
}
