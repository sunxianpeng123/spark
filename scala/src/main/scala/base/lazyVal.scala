package base

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/12
  * \* Time: 18:36
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object lazyVal {
  def main(args: Array[String]): Unit = {
    lazy val resource:Int =init()
   //lazy表示只在需要时才会执行计算，
    //不可以修饰var
    //具有额外开销，不可随意使用

  }
  def init():Int={
    0
  }
}

