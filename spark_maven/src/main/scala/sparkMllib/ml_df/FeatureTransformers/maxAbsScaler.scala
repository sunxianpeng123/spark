package sparkMllib.ml_df.FeatureTransformers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.MaxAbsScaler
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

object maxAbsScaler {
  /**
    * MaxAbsScaler（绝对值规范化）
    * MaxAbsScaler转换Vector行的数据集，通过划分每个要素中的最大绝对值，将每个要素的重新映射到范围[-1,1]。 它不会使数据移动/居中，因此不会破坏任何稀疏性。
    * MaxAbsScaler计算数据集的统计信息，并生成MaxAbsScalerModel。 然后，模型可以将每个要素单独转换为范围[-1,1]。
    * 以下示例演示如何以libsvm格式加载数据集，然后将每个要素重新缩放为[-1,1]。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("BinarizerExample")
      .master("local[*]")
      .getOrCreate()
    val dataFrame = spark.createDataFrame(Seq(
      (0, Vectors.dense(1.0, 0.1, -8.0)),
      (1, Vectors.dense(2.0, 1.0, -4.0)),
      (2, Vectors.dense(4.0, 10.0, 8.0))
    )).toDF("id", "features")

    val scaler = new MaxAbsScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")

    // Compute summary statistics and generate MaxAbsScalerModel
    val scalerModel = scaler.fit(dataFrame)

    // rescale each feature to range [-1, 1]
    val scaledData = scalerModel.transform(dataFrame)
    scaledData.select("features", "scaledFeatures").show()
  }
}
