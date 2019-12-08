package sparkMllib.ml_df.FeatureExtractors

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.PolynomialExpansion
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/3
  * \* Time: 16:50
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object polynomialExpansion {
  //Polynomial expansion （多项式展开）是将特征扩展为多项式空间的过程，多项式空间由原始维度的n度组合组成。
  // PolynomialExpansion类提供此功能。 下面的例子显示了如何将您的功能扩展到3度多项式空间。
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val data = Array(
      Vectors.dense(2.0, 1.0),
      Vectors.dense(0.0, 0.0),
      Vectors.dense(3.0, -1.0)
    )
    val df = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")

    val polyExpansion = new PolynomialExpansion()
      .setInputCol("features")
      .setOutputCol("polyFeatures")
      .setDegree(3)

    val polyDF = polyExpansion.transform(df)
    polyDF.show(false)

  }
}

