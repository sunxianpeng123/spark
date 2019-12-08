package wordSeg

import java.io.RandomAccessFile

import scala.io.Source

/**
  * Created by Dell on 2017/7/14.
  */
object transdic {
  def main(args: Array[String]): Unit = {
    val outpath="F:\\ScalaProjects\\test_scala\\default.dic"
    val raf=new RandomAccessFile(outpath,"rw")

    Source.fromFile("src/main/resources/dict.txt").getLines().foreach(line=>{
      val arr=line.split(" ")
      if(arr.length==3){
        val writLine=arr(0)+" "+arr(2)+" "+arr(1)
        //        println(writLine)
        //        println(arr.length)
        raf.write(writLine.getBytes())
        raf.write("\n".getBytes())
      }else{
        raf.write(arr(0).getBytes())
        raf.write("\n".getBytes())
      }
    })
    Source.fromFile("src/main/resources/dict.big.txt").getLines().foreach(line=>{
      val arr=line.split(" ")
      if(arr.length==3){
        val writLine=arr(0)+" "+arr(2)+" "+arr(1)
        //        println(writLine)
        //        println(arr.length)
        raf.write(writLine.getBytes())
        raf.write("\n".getBytes())
      }
    })
  raf.close()
  }
}
