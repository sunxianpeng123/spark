package pattern_match

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/19
  * \* Time: 17:07
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object MatchTuple {
  def main(args: Array[String]): Unit = {
  val langs=Seq(
    ("scala","martin","ordersky"),
    ("clojure","rich","hickey"),
    ("lisp","john","mccarthy")
  )
    for (tup<-langs){
      tup match {
        case ("scala",_,_)=>println("found scala")
        case (lang,first,last)=>println(s"found other language ($first,$last)")
      }
    }

  }
}

