import org.apache.spark.{SparkConf, SparkContext}
import org.apdplat.word.WordSegmenter
import org.apdplat.word.segmentation.SegmentationAlgorithm
import scala.collection.JavaConversions._
/**
  * Created by Dell on 2017/6/15.
  */
object test1 {
  def main(args: Array[String]): Unit = {
    val conf =new SparkConf().setMaster("local").setAppName("myapp")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    val oneTwo = List(1, 2)

    val threeFour = List(3, 4)

    val oneTwoThreeFour = oneTwo ::: threeFour
    println(oneTwoThreeFour)

    val words = WordSegmenter.seg("你好， 我是项佳", SegmentationAlgorithm.BidirectionalMaximumMatching)
    println(words.getClass)
    words.foreach(println)
  }
}
