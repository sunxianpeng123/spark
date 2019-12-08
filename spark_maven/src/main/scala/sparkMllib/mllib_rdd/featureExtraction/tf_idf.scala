package sparkMllib.mllib_rdd.featureExtraction

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.feature.{HashingTF, IDF}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
object tf_idf {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    // 加载文件（每行一个）
    //val path="F:\\ScalaProjects\\spark_test_maven\\data\\mllib\\kmeans_data.txt"
    val path="data/mllib/kmeans_data.txt"
    val documents: RDD[Seq[String]] = spark.sparkContext.textFile(path)
      .map(_.split(" ").toSeq)

    import spark.implicits._//toDF和toDS需要导入此包
    documents.toDF().show()

    val hashingTF =new HashingTF()
    val tf:RDD[Vector]=hashingTF.transform(documents)

    tf.take(1).foreach(println)
    tf.take(1).foreach(row=>{println(row.size)})

    // 在应用 HashingTF 时，只需要单次传递数据，应用 IDF 需要两次：
    // 首先计算 IDF 向量，其次用 IDF 来缩放字词频率。
    tf.cache()
    val idf=new IDF().fit(tf)
    val tfidf:RDD[Vector]=idf.transform(tf)

    tfidf.take(1).foreach(println)
    tfidf.take(1).foreach(row=>{println(row.size)})

    // spark.mllib IDF 实现提供了忽略少于最少文档数量的字词的选项。
    //在这种情况下，这些条款的 IDF 设置为0。
    // 通过将minDocFreq值传递给IDF构造函数，可以使用此功能。
    val idfIgnore=new IDF(minDocFreq = 2).fit(tf)
    val tfidfignore:RDD[Vector] =idfIgnore.transform(tf)

  }
}
