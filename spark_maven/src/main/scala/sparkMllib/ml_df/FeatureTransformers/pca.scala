package sparkMllib.ml_df.FeatureTransformers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.{PCA, StandardScaler}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

object pca {
  /**
    *
    * 注意：PCA前一定要对特征向量进行规范化（标准化）！！！
    * 没有标准化特征向量，直接进行PCA主成分：各主成分之间值变化太大，有数量级的差别。
    * 标准化特征向量后PCA主成分，各主成分之间值基本上在同一水平上，结果更合理
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("TokenizerExample")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._
    val data = Array(
      Vectors.sparse(5, Seq((1, 1.0), (3, 7.0))),
      Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0),
      Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0)
    )
    val df = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")
    //首先对特征向量进行标准化
    val  scaledDataFrame=new StandardScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setWithMean(false)//对于稀疏数据（如本次使用的数据），不要使用平均值
      .setWithStd(true)
      .fit(df)
      .transform(df)

    //PCA Model
    val pca = new PCA()
      .setInputCol("scaledFeatures")
      .setOutputCol("pcaFeatures")
      .setK(3)
      .fit(scaledDataFrame)
    //进行PCA降维
    val result = pca.transform(scaledDataFrame)
    result.show(false)
  }
}
