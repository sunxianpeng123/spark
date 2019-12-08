package base

import scala.annotation.tailrec
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by XP on 2017/9/28.
  */
object concurrence_future {
  def main(args: Array[String]): Unit = {
    def sleep(millis:Long){Thread.sleep(millis)}
    def doWork(index:Int){sleep((math.random*1000).toLong)}
    (1 to 5) foreach{index=>
      val future =Future{
        doWork(index)
      }
      future onSuccess{
        case answer:Unit => println(s"Success ! returned :$answer")
      }
      future onFailure{
        case  th:Throwable => println(s"Failure ! returned :$th")
      }
    }

    sleep(1000)
    println("Finto")
    //2  嵌套方法和递归
    def fantorial(i:Int):Long={
      @tailrec//自动检测代码是否实现尾递归
      def fact(i:Int,accumulator:Int):Long={
        if (i<=1) accumulator
        else fact(i-1,i*accumulator)
      }
      fact(i,1)
    }
    (0 to 5) foreach(i=>{println(fantorial(i))})


  }
}
