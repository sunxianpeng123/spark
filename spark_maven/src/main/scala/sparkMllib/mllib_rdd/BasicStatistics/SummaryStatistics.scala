package sparkMllib.mllib_rdd.BasicStatistics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, Statistics}
import org.apache.spark.sql.SparkSession

object SummaryStatistics {
  /**
    * Summary statistics/概要统计
    * 我们提供对 RDD[Vector] 中的向量的统计,采用 Statistics 里的函数 colStats() 返回一个
    * MultivariateStatisticalSummary 的实例, 包含列的最大值、最小值、均值、方差和非零的数量以及总数量。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val observations = spark.sparkContext.parallelize(
      Seq(
        Vectors.dense(1.0, 10.0, 100.0),
        Vectors.dense(2.0, 20.0, 200.0),
        Vectors.dense(3.0, 30.0, 300.0)
      )
    )

    // 计算概要统计.
    val summary: MultivariateStatisticalSummary = Statistics.colStats(observations)
    println(summary.mean)  //  每一列的均值
    println(summary.variance)  // 所有向量的方差
    println(summary.numNonzeros)  // 每列中的非零数
    println(summary.max)//每一列
    println(summary.min)
    println(summary.count)
  }
}
