package sparkMllib.ml_df.FeatureExtractors

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}
object countVectorizer {
  /**
    * CountVectorizer旨在通过计数来将一个文档转换为向量。当不存在先验字典时，Countvectorizer作为Estimator提取词汇进行训练，
    * 并生成一个CountVectorizerModel用于存储相应的词汇向量空间。该模型产生文档关于词语的稀疏表示，其表示可以传递给其他算法，例如LDA。
    * 在CountVectorizerModel的训练过程中，CountVectorizer将根据语料库中的词频排序从高到低进行选择，词汇表的最大含量由vocabsize超参数来指定
    * ，超参数minDF，则指定词汇表中的词语至少要在多少个不同文档中出现。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val df = spark.createDataFrame(Seq(
      (0, Array("a", "b", "c")),
      (1, Array("a", "b", "b", "c", "a"))
    )).toDF("id", "words")

    // fit a CountVectorizerModel from the corpus
    val cvModel: CountVectorizerModel = new CountVectorizer()
      .setInputCol("words")
      .setOutputCol("features")
      .setVocabSize(3)
      .setMinDF(2)
      .fit(df)
    df.show()
    // alternatively, define CountVectorizerModel with a-priori vocabulary
    val cvm = new CountVectorizerModel(Array("a", "b", "c"))
      .setInputCol("words")
      .setOutputCol("features")

    cvModel.transform(df).show(false)
  }
}
