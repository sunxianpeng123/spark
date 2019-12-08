package sparkMllib.ml_df.FeatureExtractors

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.sql.SparkSession
import scala.collection.JavaConversions._
object tfidf {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val sentenceData = spark.createDataFrame(Seq(
      (0.0, "I heard about Spark and I love Spark"),
      (0.0, "I wish Java could use case classes"),
      (1.0, "Logistic regression models are neat")
    )).toDF("label", "sentence")

    val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
    val wordsData = tokenizer.transform(sentenceData)

    val hashingTF = new HashingTF()
      .setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(2000)//哈希表的桶数为2000

    val featurizedData = hashingTF.transform(wordsData)
    // alternatively, CountVectorizer can also be used to get term frequency vectors
    println("##########看到每一个单词被哈希成了一个不同的索引值。以\"I heard about Spark and I love Spark\"为例，输出结果中2000代表哈希表的桶数，\n" +
      "“[240,333,1105,1329,1357,1777]”分别代表着“i, spark, heard, about, and, love”的哈希值，“[1.0,1.0,2.0,2.0,1.0,1.0]”为对应单词的出现次数。#####")
    println("---------")
    featurizedData.foreach(row=>{println(row)})
    val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
    val idfModel = idf.fit(featurizedData)

    val rescaledData = idfModel.transform(featurizedData)
    //rescaledData.select("label", "features").show()
    rescaledData.show()
    println("http://dblab.xmu.edu.cn/blog/1261-2/")
    println("#########[240,333,1105,1329,1357,1777]”分别代表着“i, spark, heard, about, and, love”的哈希值。240和333分别代表了“i\"和\"spark\"，\n" +
      "其TF-IDF值分别是0.6931471805599453和0.6931471805599453。######")
    println("---------")
    rescaledData.select("features","label").take(3).foreach(row=>{println(row)})
  }
}
