package base

/**
  * Created by XP on 2017/9/27.
  */
object variate_stat {
  def main(args: Array[String]): Unit = {
    val array:Array[String] =new Array[String](5)
    array(0)="hello"//array不可变，里面元素可变
    var  stoc:Double =100.0
    stoc=200.0//stoc可变，但是stoc的类型double不可变
    //一般情况变量需要立即初始化值，但是在作为函数参数时例外
  }
}
