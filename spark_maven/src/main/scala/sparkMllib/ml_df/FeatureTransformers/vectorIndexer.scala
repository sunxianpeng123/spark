package sparkMllib.ml_df.FeatureTransformers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

object vectorIndexer {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("BinarizerExample")
      .master("local[*]")
      .getOrCreate()

    val data = Seq(Vectors.dense(-1.0, 1.0, 1.0),Vectors.dense(-1.0, 3.0, 1.0), Vectors.dense(0.0, 5.0, 1.0))
    val df = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")
    /**
      * 设置maxCategories为2，即只有种类小于2的特征才被认为是类别型特征，否则被认为是连续型特征。
      * 其中类别型特征将被进行编号索引，为了索引的稳定性， -规定-  如果这个特征值为0，则一定会被编号成0，这样可以保证向量的稀疏度
      * （未来还会再维持索引的稳定性上做更多的工作，比如如果某个特征类别化后只有一个特征，则会进行警告等等，这里就不过多介绍了）。
      * 于是，我们可以看到第0类和第2类的特征由于种类数不超过2，被划分成类别型特征，并进行了索引，且为0的特征值也被编号成了0号。
      */
    val indexer = new VectorIndexer().
             setInputCol("features").
             setOutputCol("indexed").
             setMaxCategories(2)

    val indexerModel = indexer.fit(df)
    val categoricalFeatures: Set[Int] = indexerModel.categoryMaps.keys.toSet

    println(s"Chose ${categoricalFeatures.size} categorical features: " + categoricalFeatures.mkString(", "))

    val indexedData = indexerModel.transform(df)
    indexedData.show()
  }
}
