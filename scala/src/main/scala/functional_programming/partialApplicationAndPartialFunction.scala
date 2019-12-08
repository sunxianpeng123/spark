package functional_programming

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/17
  * \* Time: 18:32
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object partialApplicationAndPartialFunction {

  def main(args: Array[String]): Unit = {
    //partial  Application Function
    val hello=cat1("Hello ") _
    println(hello)
    println(hello ("World!"))
    println(cat1("Hello ")("World!"))
    println("===================")
    //partial  Function
    val inverse:PartialFunction[Double,Double]={
      case d if d != 0.0 => 1.0/d
    }
    println(inverse(1.0))
    println(inverse(2.0))
//    println(inverse(0.0))


  }
  def cat1(s1:String)(s2:String)=s1+s2


}

