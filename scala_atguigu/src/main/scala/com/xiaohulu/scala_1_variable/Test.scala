


/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/21
  * \* Time: 17:22
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object Test {
  def main(args: Array[String]): Unit = {
    val res = sayHi
    println(res)
    val dog :Dog = null
//    val char1 :Char = null//下划线表示隐式转化，这一行不报错
  }
  def sayHi():Unit={
//    throw  new Exception ("exception")
    println("被调用")
  }
}

class Dog{
}

