package string

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sun
  * \* Date: 2017/9/20
  * \* Time: 15:15
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object testString {
  def main(args: Array[String]): Unit = {
    //1.1
    println("scala".drop(2).take(2).capitalize)
    val str_1="hello"
    str_1.getBytes().foreach(println)
//1.2
    println(
      """ Four score and
        | seven years ago
      """.stripMargin)
//1.4
    val name ="Fred"
    val age =33
    val weights=200.0
    println(s"$name is ${age ==1} years old , and weights ${weights + 1} pounds")
//
    case class Student(name:String,score:Int)
    val hannah=Student("Hannah",95)
    println(s"${hannah.name} has a score of ${hannah.score}")
    println(s"$hannah.name has a score of $hannah.score")
   println(f"$name is ${age ==1} years old , and weights $weights%.2f pounds")
    //5
    println(s"foo\nbar")
    println(raw"foo\nbar")
//
    val s ="%s is %d years old".format(name,age)
    println(s)
  //1.5
    val upper="hello ,world".map(_.toUpper).foreach(print)
    println()
    val upper_filter="hello,world".filter(_ !='l').map(_.toUpper).foreach(print)//单引号与双引号有不同
    println()
    val upper_yeild=for(c<-"hello,world") yield c.toUpper
    println(upper_yeild)

    //
    def toLower(c:Char):Char=(c.toByte+32).toChar
    "HELLO".map(toLower).foreach(print)
    println()
//1.6
    val numPattern="[0-9]+".r
    val address="123 Main Street Suite 101"
    val match1=numPattern.findFirstIn(address)
    println("====")
    val match2=numPattern.findAllIn(address).toArray
    match2.foreach(println)
//1.7
    val address_replace=address.replaceAll("[0-9]","x")
    println(address_replace)
    val regex="[0-9]".r
    val new_address=regex.replaceAllIn(address,"x")
    println(new_address)
    val new_add_first=regex.replaceFirstIn(address,"x")
    val address_replace_first=address.replaceFirst("[0-9]","x")
    println(new_add_first)
    println(address_replace_first)
    //1.8
    val pattern="([0-9]+) ([A-Za-z]+)".r
    val pattern(count,fruit)="100 bananas"
    println(count)
    println(fruit)
//1.9
    println("hello".charAt(0))
    println("hello"(0))


  }
}

