package pattern_match

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/19
  * \* Time: 16:43
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object MatchValTypePara {
  def main(args: Array[String]): Unit = {
    checkY(100)

  }
  def checkY(y:Int):Unit={
    for{
      x<-Seq(99,100,101)
    }{
      val str=x match {
        case 'y' =>"found y"//case y匹配所有输入，并将其赋给新的y,case 'y'
        case i:Int=>"int :"+i
      }
      println(str)
    }
  }
}
