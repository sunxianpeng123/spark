package wordSeg

import java.io.FileInputStream
import java.sql.DriverManager
import java.util
import java.util.Properties

import org.ansj.library
import org.ansj.library.{DicLibrary, StopLibrary}
import org.ansj.recognition.impl.StopRecognition
import org.ansj.splitWord.analysis.DicAnalysis
import org.ansj.util.MyStaticValue
import org.nlpcn.commons.lang.tire.domain.{Forest, Value}
import org.nlpcn.commons.lang.tire.library.Library

import scala.collection.JavaConversions._
/**
  * Created by Dell on 2017/7/11.
  */
object testAnsj {
  def main(args: Array[String]): Unit = {
    // 增加新词,中间按照'\t'隔开
    DicLibrary.insert(DicLibrary.DEFAULT,"增加新词","我是词性",1000)
    println(DicLibrary.DEFAULT.size)
    println("============")
    val result1=DicAnalysis.parse("这是用户自定义词典增加新词的例子")
    println(result1)
    // 删除词语,只能删除.用户自定义的词典.
    DicLibrary.delete(DicLibrary.DEFAULT, "增加新词")
    val result2=DicAnalysis.parse("这是用户自定义词典增加新词的例子")
    println(result2)
    println("=============")
    val dict1 = new Forest()
    Library.insertWord(dict1, new Value("苹果", "userDefine", "1000"))
    Library.insertWord(dict1, new Value("咖啡", "userDefine", "1000"))
    val result3=DicAnalysis.parse("吃苹果喝咖啡",dict1)
    result3.foreach(println)
    println("==========")
    //
    val userStopList:util.List[String]=new util.ArrayList[String]()
    userStopList.add("的")
    userStopList.add("小")
    userStopList.add("英文版")

    var  filter =new  StopRecognition()
    filter.insertStopWords(userStopList)
    val result4=DicAnalysis.parse("英文版是小田亲自翻译的")
    result4.foreach(print)
    println("\n=======================")
    println("+++++++++++++++++")
    System.out.println(result4.recognition(filter)foreach(println))

  }
}
