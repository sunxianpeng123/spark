package sparkMllib.ml_df.FeatureTransformers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.sql.SparkSession

object standardScaler {
  /**
    * StandardScaler（标准化）
    * StandardScaler转换Vector行的数据集，使每个要素标准化以具有单位标准偏差和 或 零均值。它需要参数：
    * withStd：默认为True。将数据缩放到单位标准偏差。
    * withMean：默认为false。在缩放之前将数据中心为平均值。它将构建一个密集的输出，所以在应用于稀疏输入时要小心。
    * StandardScaler是一个Estimator，可以适合数据集生成StandardScalerModel; 这相当于计算汇总统计数据。 然后，模型可以将数据集中的向量列转换为具有单位标准偏差和/或零平均特征。
    * 请注意，如果特征的标准偏差为零，它将在该特征的向量中返回默认的0.0值。
    *
    *   /**
    * 标准化是指：对于训练集中的样本，基于列统计信息将数据除以方差或（且）者将数据减去其均值（结果是方差等于1，数据在 0 附近）。这是很常用的预处理步骤。
    * 例如，当所有的特征具有值为 1 的方差且/或值为 0 的均值时，SVM 的径向基函数（RBF）核或者 L1 和 L2 正则化线性模型通常有更好的效果。
    * 标准化可以提升模型优化阶段的收敛速度，还可以避免方差很大的特征对模型训练产生过大的影响。
    * @param args
    */
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("BinarizerExample")
      .master("local[*]")
      .getOrCreate()
    val dataFrame = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")

    val scaler = new StandardScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setWithStd(true)
      .setWithMean(false)

    // Compute summary statistics by fitting the StandardScaler.
    val scalerModel = scaler.fit(dataFrame)

    // Normalize each feature to have unit standard deviation.
    val scaledData = scalerModel.transform(dataFrame)
    scaledData.show()
  }
}
