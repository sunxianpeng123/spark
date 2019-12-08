package com.xiaohulu

/**
  * Hello world!
  *
  */
object App  {
  def main(args: Array[String]): Unit = {
    (0 to  5).foreach(code=>{
      code match {
        case 1 | 2 => println("1 or 2") // whatever
        case 3 =>println("3")
        case _ =>println("other")
      }
    })
  }

}
