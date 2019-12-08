package sparkMllib.mllib_rdd.featureExtraction

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.feature.{HashingTF, IDF, Word2Vec, Word2VecModel}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object word2Vec {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    // 加载文件（每行一个）
    val input = spark.sparkContext.textFile("data/mllib/sample_lda_data.txt").map(line => line.split(" ").toSeq)

    import spark.implicits._//toDF和toDS需要导入此包
    input.toDF().show()

    val word2vec = new Word2Vec()
    val model = word2vec.fit(input)
    val synonyms = model.findSynonyms("1", 5)

    for((synonym, cosineSimilarity) <- synonyms) {
      println(s"$synonym $cosineSimilarity")
    }

    // 保存和加载模型
//    model.save(spark.sparkContext, "myModelPath")
//    val sameModel = Word2VecModel.load(spark.sparkContext, "myModelPath")
  }
}
