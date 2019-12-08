package sparkMllib.mllib_rdd.BasicStatistics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object HypothesisTesting {
  /**
    * Hypothesis testing/假设检验
    * 假设检验是统计学中强大的工具，用于确定结果是否具有统计学意义，无论该结果是否偶然发生。
    * spark.mllib目前支持Pearson的卡方（χ2χ2）测试，以获得拟合优度和独立性。
    * 输入数据类型确定是否进行拟合优度或独立性测试。 拟合优度测试需要输入类型的Vector，而独立性测试需要一个 Matrix作为输入。
    * spark.mllib还支持输入类型 RDD[LabeledPoint] 通过卡方独立测试启用特征选择。
    * Statistics 提供运行Pearson卡方检验的方法。
    * 以下示例演示如何运行和解释假设检验。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
  }
}
