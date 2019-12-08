package functional_programming

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/17
  * \* Time: 18:52
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object CurryOptimize {
  /**
    * 一个带有多个参数的函数转换成一系列函数，每个函数只有一个参数
    * @param args
    */
  def main(args: Array[String]): Unit = {
    println("========1、no curry============")
    //1、no curry
    val hello=cat1("Hello ") _
    println(hello)
    println(hello ("World!"))
    println(cat1("Hello ")("World!"))
    println("========//with curry===========")
    //with curry
    val cat2Hello=cat2("Hello ")
    println(cat2Hello("World!"))
    println("=========2、一个带有多个参数的方法的Curry化==========")
//2、一个带有多个参数的方法的Curry化
    println(cat3("hello" ,"world"))
    val cat3Curried =(cat3 _).curried
    println(cat3Curried("Hello ")("World!"))
    //3、去Curry化
    val cat3Uncurried=Function.uncurried(cat3Curried)
    println(cat3Uncurried("hello ","world!"))
    println("========简化函数--作用===========")
    //4、简化函数--作用
    val d3=(2.2,3.3,4.4)
    println(mult(d3._1,d3._2,d3._3))

    val multTupled=Function.tupled(mult _)
    println(multTupled(d3))
    val multUntupler=Function.untupled(multTupled)
    println(multUntupler(d3._1,d3._2,d3._3))
    println("========5、偏函数与返回option函数之间的相互转化===========")
    //5、偏函数与返回option函数之间的相互转化
    val finicky:PartialFunction[String,String]={
      case "finicky"=>"FINICKY"
    }
    println(finicky("finicky"))
//    println(finicky("other"))//会报错
    val finickyOption=finicky.lift
    println(finickyOption("finicky"))
    println(finickyOption("finicky").get)
    println(finickyOption("other"))
    val finicky2=Function.unlift(finickyOption)
    println(finicky2("finicky"))
//    println(finicky2("other"))//会报错


  }
  def cat1(s1:String)(s2:String)=s1+s2
  def cat2(s1:String)=(s2:String)=>s1+s2
  def cat3(s1:String,s2:String) = s1+s2
  def mult(d1:Double,d2:Double,d3:Double)=d1*d2*d3


}

