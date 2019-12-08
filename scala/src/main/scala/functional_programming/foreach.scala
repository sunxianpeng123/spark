package functional_programming

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/25
  * \* Time: 18:35
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object foreach {
  def main(args: Array[String]): Unit = {
    val stateCaptial=Map(
      "a"->"123",
      "b"->"456"
    )
    println("=========")
    stateCaptial.foreach(kv=>{
      println(kv._1)
      println(kv._2)
    })
    println("============")
    stateCaptial.foreach{case(k,v)=>{
      println(k+"   "+v)
    }}


  }
}

