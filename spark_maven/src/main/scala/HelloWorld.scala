import java.io.{FileNotFoundException, FileReader, IOException}

import scala.util.matching.Regex

/**
  * Created by Dell on 2017/6/9.
  */
object HelloWorld {
  def main(args: Array[String]): Unit = {

    val myInt : Int=1
    val a,b=100
    println(a,b)
    val tup=(40,"Foo")
    println(tup)

    //
    val pattern="scala".r
    val str="scala is Scalable and cool"
    println(pattern findFirstIn str)
//
    val pattern1=new Regex("(S|s)cala")
    println((pattern1 findAllIn str).mkString(","))
//
    println(pattern1.replaceAllIn(str,"java"))
//
    val pattern2 = new Regex("abl[ae]\\d+")
    val str1 = "ablaw is able1 and cool"
    println((pattern2 findAllIn str1).mkString(","))
//
    println("hello world")
    var myVar : String = "Foo"
    println(myVar)
//
    try {
      val f = new FileReader("input.txt")
    } catch {
      case ex: FileNotFoundException =>{
        println("Missing file exception")
      }
      case ex: IOException => {
        println("IO Exception")
      }
    }finally {
      println("Exiting finally...")
    }
//

  }
}
