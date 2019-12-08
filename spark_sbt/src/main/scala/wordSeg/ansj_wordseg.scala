package wordSeg

import org.ansj.domain.Result
import org.ansj.recognition.impl.StopRecognition
import org.ansj.splitWord.analysis.DicAnalysis
import org.nlpcn.commons.lang.tire.domain.{Forest, Value}
import org.nlpcn.commons.lang.tire.library.Library

import scala.collection.JavaConversions._
import scala.io.Source
/**
  * Created by Dell on 2017/7/11.
  */
object ansj_wordseg {
  def main(args: Array[String]): Unit = {
    val str= "测试一下自定义用户字典新增词字典和停用词字典的使用方法"
    //用户字典
    var dic=new Forest()
    Library.insertWord(dic, new Value("新增词","词性","1000"))
    Library.insertWord(dic, new Value("停用词","词性","1000"))
    //stopwords
    var filter =new  StopRecognition()
    Source.fromFile("src/main/resources/stopWord.txt").getLines().foreach(line=>
      filter.insertStopWords(line)
    )

    //val result_1 =DicAnalysis.parse(str)
    val words:Result=seg(str,dic,filter)//
    //println(words.getClass)
    words.foreach(word=>{
      println(word.toString.split("/")(0))
    })
  }

  def seg(segStr:String,dic:Forest,filter:StopRecognition):Result={
  //  segStr待分词字符串，customList自定义用户词典，跳过word从txt文件加载字典的步骤，直接从数据库中取词
  //分词
    var words=DicAnalysis.parse(segStr,dic)
    words=words.recognition(filter)
    //words.foreach(println)
    words
  }
}
