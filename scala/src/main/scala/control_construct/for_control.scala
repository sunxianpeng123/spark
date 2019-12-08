package control_construct

import scala.util.control.Breaks

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sun
  * \* Date: 2017/9/20
  * \* Time: 18:53
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object for_control {
  def main(args: Array[String]): Unit = {
    val a =Array("apple","banana","orange")
    //3.1
    for(e<- a ) println(e)
    a.foreach(println)
    //return in for
    val newArray=for(e<-a) yield {val s =e.toUpperCase
      s
    }
    println(newArray)
    //counter of for
    for((e,count)<-a.zipWithIndex){println(s"$count is $e")}
    //scann a map
    val names=Map("fname"->"robert","lname"->"goren")
    for((k,v)<-names) println(s"key :$k  value :$v")
    //3.2  mulity counter in for
    for(i<-1 to 2 ;j <- 1 to 5; k<- 1 to 10) println(s"i = $i , j = $j, k= $k")
    //3.3  use mulity if in for
    for(i<- 1 to 10 if i% 2 ==0 if i>3 if i <6) println(i)

    //3.4 for/yield
    val names_fory=Array("chris","ed","maurice")
    val capnames=for(e<- names_fory) yield e.capitalize
    println(capnames)
    val lengths=for(e<-names_fory) yield {e.length}
    //3.5 break  and continus
    println("==========break example==============")
    import util.control.Breaks._
    breakable{
      for(i<- 1 to 10){
        println(i)
        if(i>4) break
      }
    }
    println("================continus example===================")
    val search_me="perter piper picked a peck of pickled peppers"
    var numPs=0
    for(i <- 0 until search_me.length){
      breakable{
        if(search_me.charAt(i) != 'p'){
          break()
        }else{
          numPs+=1
        }
      }
    }
    println(s"Found $numPs p's in the string")
    println("multi for  and labeled break")
    val inner=new Breaks
    val outter=new Breaks
    outter.breakable{
      for (i <- 1 to 5){
        inner.breakable{
          for(j<- 'a' to 'e'){
            if (i==1 && j=='c') inner.break else println(s"i: $i , j :$j")
            if(i==2 && j=='b') outter.break
          }
        }
      }
    }
//3.6  use if else like three elements
    var aaa=1
    val absValue= if (aaa < 0) -aaa else aaa
//3.7 use switch
    val month_1=1
    month_1 match {
      case 1=>println("January")
    }
    val i =1
    val month_2 = i match {
      case 1=>"January"
      case 12 => "December"
      case _=>"Invalid month"//default
    }
    println(month_2)
//3.8 use case to mathce multi condition
    val i1 =5
    i1 match{
      case 1|3|5|7|9 =>println("odd")
      case 2|4|6|8|10 => println("even")
      case _ =>println("doing nothing")
    }
    //3.10 use default match but not _
    val ii1=122
    ii1 match {
      case 0 =>println("1")
      case 1 => println("2")
      case default =>println("you give me :"+default)
    }
    ii1 match {
      case 0 =>println("1")
      case 1 => println("2")
      case whoa =>println("you give me :"+whoa)
      //
    }
    //3.12 use if in case
    ii1 match {
      case  a if 0 to 9  contains a => println(a)
      case b if 10 to 19 contains b =>println(b)
      case _ =>println("Hmmm...")
    }
    //3.15 use list in match
    val x =List("a","b","c")
    val y =1::2::3::Nil
    val xx=listtostring(x)
    println("*************")
    println(xx)

    //use try/catch
    val s="Foo"
    try {
      val i =s.toInt
    }catch {
      case e: Exception=>e.printStackTrace
    }finally {
      println("aaaa")
    }



  }

  ////3.11 multi pattern match in case
  def echoWhatYouGiveme(x:Any):String=x match {
    case 0 => "zero"
    case true =>"true"
    case "hello" => "you said 'hello'"
    //you can add any pattern you like
    case _=>"unknow"
  }
//3.15
  def listtostring(list:List[String]):String=list match {
    case s:: rest => s+ " " +listtostring(rest)
    case Nil=>""
  }
}

