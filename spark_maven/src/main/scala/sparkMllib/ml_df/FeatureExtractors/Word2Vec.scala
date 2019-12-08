//package sparkMllib.ml_df.FeatureExtractors
//
//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.ml.feature.Word2Vec
//import org.apache.spark.sql.SparkSession
////import org.apache.spark.ml.feature.Word2Vec
//import org.apache.spark.ml.linalg.Vector
//import org.apache.spark.sql.Row
//object Word2Vec {
////  Word2vec是一个Estimator，它采用一系列代表文档的词语来训练word2vecmodel。该模型将每个词语映射到一个固定大小
////  的向量。word2vecmodel使用文档中每个词语的平均数来将文档转换为向量，然后这个向量可以作为预测的特征，来计算文档相似度计算等等。
//  def main(args: Array[String]): Unit = {
//    Logger.getLogger("org").setLevel(Level.ERROR)
//    val spark=SparkSession.builder().master("local[*]").appName("test word2vec").getOrCreate()
//    // Input data: Each row is a bag of words from a sentence or document.
//    val documentDF = spark.createDataFrame(Seq(
//      "Hi I heard about Spark".split(" "),
//      "I wish Java could use case classes".split(" "),
//      "Logistic regression models are neat".split(" ")
//    ).map(Tuple1.apply)).toDF("text")
//
//    // Learn a mapping from words to Vectors.
//    val word2Vec = new Word2Vec()
//      .setInputCol("text")
//      .setOutputCol("result")
//      .setVectorSize(3)//维度为3
//      .setMinCount(0)
//    val model = word2Vec.fit(documentDF)
//
//    val result = model.transform(documentDF)
//    result.collect().foreach { case Row(text: Seq[_], features: Vector) =>
//      println(s"Text: [${text.mkString(", ")}] => \nVector: $features\n") }
//  }
//}
