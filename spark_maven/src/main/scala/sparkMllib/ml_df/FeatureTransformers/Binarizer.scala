package sparkMllib.ml_df.FeatureTransformers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.Binarizer
import org.apache.spark.sql.SparkSession

object Binarizer {
  /**
    * Binarizer（二值化）
    * Binarization （二值化）是将数值特征阈值化为二进制（0/1）特征的过程。
    * Binarizer（ML提供的二元化方法）二元化涉及的参数有 inputCol（输入）、outputCol（输出）以及threshold（阀值）。
    * （输入的）特征值大于阀值将二值化为1.0，特征值小于等于阀值将二值化为0.0。inputCol 支持向量（Vector）和双精度（Double）类型。
    * @param args
    */
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("TokenizerExample")
      .master("local[*]")
      .getOrCreate()
    val data = Array((0, 0.1), (1, 0.8), (2, 0.2))
    val dataFrame = spark.createDataFrame(data).toDF("id", "feature")

    val binarizer: Binarizer = new Binarizer()
      .setInputCol("feature")
      .setOutputCol("binarized_feature")
      .setThreshold(0.5)

    val binarizedDataFrame = binarizer.transform(dataFrame)

    println(s"Binarizer output with Threshold = ${binarizer.getThreshold}")
    binarizedDataFrame.show()
  }
}
