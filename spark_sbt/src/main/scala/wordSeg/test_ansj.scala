package wordSeg

import java.util.Date

import org.ansj.splitWord.analysis.DicAnalysis

/**
  * Created by Dell on 2017/7/14.
  */
object test_ansj {
  def main(args: Array[String]): Unit = {
    val str= "测试一下自定义用户字典新增词字典和停用词字典的使用方法"
    val start=new Date().getTime
    val words=DicAnalysis.parse(str)
    println(words)
    val  end=new  Date().getTime
    println(end-start)
    val str1= "Java 中一个非常常用的类,该类用来对日期字符串进行解析和格式化输出,但如果使用不小心会导致非常微妙和难以调试的问题"
    val start1=new Date().getTime
    val words1=DicAnalysis.parse(str1)
    println(words1)
    val  end1=new  Date().getTime
    println(end1-start1)
  }
}
