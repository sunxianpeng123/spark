package pattern_match

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/19
  * \* Time: 16:49
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object MatchSeq {

  def main(args: Array[String]): Unit = {
    val noEmptySeq=Seq(1,2,3,4,5)
    val emptySeq=Seq.empty[Int]
    val noEmptyList=List(1,2,3,4,5)
    val emptyList=Nil
    val noEmptyVector=Vector(1,2,3,4,5)
    val emptyVector=Vector.empty[Int]
    val noEmptyMap=Map("one"->1,"two"->2,"three"->3)
    val emptyMap=Map.empty[String,Int]


  }
//def seqToString(seq: Seq[T]):String=seq match {
//page91
//}


}

