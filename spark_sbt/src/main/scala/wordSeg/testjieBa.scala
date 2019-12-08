package wordSeg

import java.io.File

import com.huaban.analysis.jieba.JiebaSegmenter.SegMode
import com.huaban.analysis.jieba.{JiebaSegmenter, WordDictionary}
import scala.collection.JavaConversions._
/**
  * Created by Dell on 2017/7/11.
  */
object testjieBa {

  def main(args: Array[String]): Unit = {
    val dic=  WordDictionary.getInstance()
    dic.loadedPath
    dic.loadDict()
    val segStr="测试一下jieba分词怎么使用B座，以及自定义词典停用词词典的使用方法新增词"
    //
    val jiebaSeg=new JiebaSegmenter()
    val words= jiebaSeg.sentenceProcess(segStr)
    val words_1=jiebaSeg.process(segStr,SegMode.INDEX)
    val words_2=jiebaSeg.process(segStr,SegMode.SEARCH)
    println(words)
    println(words_1)
    println(words_2)

    val words1= jiebaSeg.sentenceProcess(segStr)
    println(words1)
    //
    val sens:List[String]=List("这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
      "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的")
//    sens.foreach(sen=>{
//      println(jiebaSeg.process(sen,SegMode.INDEX).toString)
//    })
  }
}
