package pattern_match

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/19
  * \* Time: 17:15
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object MatchCaseGuard {
  def main(args: Array[String]): Unit = {
    for (i <- Seq(1, 2, 3, 4)) {
      i match {
        case _ if i%2 ==0 =>println(s"even:$i")
        case _            =>println(s"odd:$i")
      }
    }
  }
}

