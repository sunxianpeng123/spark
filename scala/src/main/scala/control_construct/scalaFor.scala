package control_construct

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/12
  * \* Time: 17:05
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object scalaFor {
  def main(args: Array[String]): Unit = {
    //生成器表达式
    println("//生成器表达式========")
    val l1 =List(1,2,3,4)
    for(num <- l1)
      println(num)
    //保护式
    println("//保护式======================")
    for(num <- l1 if num%2 != 0 if num >0) println(num)
    //Yielding  不打印保护式后的数字，但是要对其进行处理，yield
    println("//Yielding  不打印保护式后的数字，但是要对其进行处理，yield===============")
    val nums = for(num <- l1 if num%2 != 0 if num >0) yield num
    println(nums)
    println("//可以在for表达式最初部分定义值，并在后面的表达式中使用该值=====================")
    //可以在for表达式最初部分定义值，并在后面的表达式中使用该值
    val dogbreeds= List("apple","bread","cat","dog")
    for {br<-dogbreeds
         upbr= br.toUpperCase()} println(upbr)
    //y


  }

}

