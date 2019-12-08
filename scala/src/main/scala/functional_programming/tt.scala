package functional_programming

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/17
  * \* Time: 17:23
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object tt {
  def main(args: Array[String]): Unit = {
    var factor =2
    val mul=(i:Int)=> i* factor
    val res=(1 to 10 ) filter(_ % 2 ==0) map mul reduce(_ *_)
    println(res)
  }
}

