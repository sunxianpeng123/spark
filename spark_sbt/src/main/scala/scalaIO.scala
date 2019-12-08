import java.io.{File, PrintWriter}

import scala.io.Source

/**
  * Created by Dell on 2017/6/13.
  */
object scalaIO {
  def main(args: Array[String]): Unit = {
//
    val writer=new PrintWriter(new File("Test.txt"))
    writer.write("this is only one Test")
    writer.close()
//
    println("请输入百度网址：")
    val line =Console.readLine()
    println(line)
//
    println("文件内容为:")
    Source.fromFile("Test.txt").foreach(
      print
    )
  }
}
