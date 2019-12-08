package base

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/12
  * \* Time: 18:45
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object testEnumeration extends  Enumeration{
  type testEnumeration =Value
  val doberman = Value("Doberman Pinscher")//
  val yorkie = Value("Yorkshire Terrier")
  val scottie =Value("Scottish Terrier")
  val dane = Value("Great Dane")
  val portie =Value("Portuguese Water Dog")
}

object weekday extends Enumeration{
  type  weekday =Value
  val mon,tue,wed,thu,fri,sat,sun=Value
}

object test{
  def main(args: Array[String]): Unit = {
    import testEnumeration._
    for(b<-testEnumeration.values)println(b.id)
    println("**********")
    testEnumeration.values//得到枚举中的每个值
      .filter(_.toString.endsWith("Terrier")) foreach(println)//tostring得到枚举值对应的Value中的别名
    println("********")
    def isTerrier(b:testEnumeration)=b.toString.endsWith("Terrier")
    println("*********")
    testEnumeration.values filter isTerrier foreach println
  import  weekday._
    weekday.values.foreach(println)
    weekday.values.foreach(x=>{println(x.toString)})
  }
}


