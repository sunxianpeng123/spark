package sparkMllib.ml_df.FeatureTransformers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

object VectorAssembler {
  /**
    * VectorAssembler 是将给定的一系列的列合并到单个向量列中的 transformer。
    * 它可以将原始特征和不同特征transformers（转换器）生成的特征合并为单个特征向量，来训练 ML 模型,如逻辑回归和决策树等机器学习算法。
    * VectorAssembler 可接受以下的输入列类型：所有数值型、布尔类型、向量类型。输入列的值将按指定顺序依次添加到一个向量中。
    * Examples
    * 假设我们有一个 DataFrame 包含 id, hour, mobile, userFeatures以及clicked 列：
          id | hour | mobile | userFeatures     | clicked
        ----|------|--------|------------------|---------
         0  | 18   | 1.0    | [0.0, 10.0, 0.5] | 1.0
    userFeatures 是一个包含3个用户特征的特征列，我们希望将 hour, mobile 以及 userFeatures 组合为一个单一特征向量叫做 features，并将其用于预测是否点击。
  如果我们设置 VectorAssembler 的输入列为 hour , mobile 以及userFeatures，输出列为 features，转换后我们应该得到以下结果：
        id | hour | mobile | userFeatures     | clicked | features
        ----|------|--------|------------------|---------|-----------------------------
         0  | 18   | 1.0    | [0.0, 10.0, 0.5] | 1.0     | [18.0, 1.0, 0.0, 10.0, 0.5]
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("BinarizerExample")
      .master("local[*]")
      .getOrCreate()
    val dataset = spark.createDataFrame(
      Seq((0, 18, 1.0, Vectors.dense(0.0, 10.0, 0.5), 1.0))
    ).toDF("id", "hour", "mobile", "userFeatures", "clicked")

    val assembler = new VectorAssembler()
      .setInputCols(Array("hour", "mobile", "userFeatures"))
      .setOutputCol("features")

    val output = assembler.transform(dataset)
    println(output.select("features", "clicked").first())


  }
}
