package wordSeg

import java.io.File

import org.apache.tools.ant.taskdefs.Java
import org.apdplat.word.WordSegmenter
import org.apdplat.word.dictionary.DictionaryFactory
import org.apdplat.word.recognition
import org.apdplat.word.recognition.StopWord
import org.apdplat.word.segmentation.SegmentationAlgorithm
import org.apdplat.word.util.WordConfTools

import scala.collection.JavaConversions._
/**
  * Created by Dell on 2017/7/10.
  */
object testＷord {
  def main(args: Array[String]): Unit = {
    val input="wordsegsource/input.txt"
    val output="wordsegsource/output.txt"

    //WordSegmenter.segWithStopWords(new File(input),new File(output))
    WordConfTools.set("stopwords.path","wordsegsource/stopWord.txt")
    StopWord.reload()
    //WordConfTools.set("dic.path", "wordsegsource/dic1.txt")
    //DictionaryFactory.reload()
//    WordSegmenter.seg(new File(input),new File(output))
    val words=WordSegmenter.seg("我是一只小小鸟，测试一下这个分词器管用么",SegmentationAlgorithm.BidirectionalMaximumMatching)
    words.foreach(println)













    System exit 0
  }
}
