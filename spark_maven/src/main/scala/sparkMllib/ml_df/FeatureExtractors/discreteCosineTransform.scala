package sparkMllib.ml_df.FeatureExtractors

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.DCT
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/3
  * \* Time: 16:35
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object discreteCosineTransform {
  //Discrete Cosine Transform（离散余弦变换） 是将时域的N维实数序列转换成频域的N维实数序列的过程（有点类似离散傅里叶
  // 变换）。（ML中的）DCT类提供了离散余弦变换DCT-II的功能，将离散余弦变换后结果乘以 得到一个与时域矩阵长度一致的矩阵。
  // 没有偏移被应用于变换的序列（例如，变换的序列的第0个元素是第0个DCT系数，而不是第N / 2个），即输入序列与输出之间是
  // 一一对应的。
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()

    val data = Seq(
      Vectors.dense(0.0, 1.0, -2.0, 3.0),
      Vectors.dense(-1.0, 2.0, 4.0, -7.0),
      Vectors.dense(14.0, -2.0, -5.0, 1.0))

    val df = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")

    val dct = new DCT()
      .setInputCol("features")
      .setOutputCol("featuresDCT")
      .setInverse(false)

    val dctDf = dct.transform(df)
    dctDf.select("featuresDCT").show(false)
  }
}

