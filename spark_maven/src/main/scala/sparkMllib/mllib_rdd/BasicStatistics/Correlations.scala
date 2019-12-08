package sparkMllib.mllib_rdd.BasicStatistics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.{Matrix, Vectors}
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Correlations {
  /**
    * Correlations/相关性
    *  计算两个系列（series）数据之间的相关性的数据是在统计学一种常见的操作。
    *  在 spark.mllib 我们提供灵活的计算两两之间的相关性的方法。支持计算相关性的方法目前有 Pearson’s and Spearman’s (皮尔森和斯皮尔曼) 的相关性.
    *  Statistics类提供了计算系列之间相关性的方法。 根据输入类型，两个RDD [Double]或RDD [Vector]，输出分别为Double或相关矩阵。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val seriesX: RDD[Double] = spark.sparkContext.parallelize(Array(1, 2, 3, 3, 5))  // a series
    // 必须具有相同数量的分区和基数作为一个series
    val seriesY: RDD[Double] = spark.sparkContext.parallelize(Array(11, 22, 33, 33, 555))

    // 计算 Pearson's 相关性.输入"spearman" 调用 Spearman's 的计算方法.
    // 默认使用 Pearson's 方法.
    //Pearson's 系数是一种线性相关系数。皮尔森相关系数是用来反映两个变量线性相关程度的统计量。相关系数用r表示，其中n为样本量，分别为两个变量的观测值和均值。r描述的是两个变量间线性相关强弱的程度。r的绝对值越大表明相关性越强。
    val correlation: Double = Statistics.corr(seriesX, seriesY, "pearson")
    println(s"Correlation is: $correlation")

    val data: RDD[Vector] = spark.sparkContext.parallelize(
      Seq(
        Vectors.dense(1.0, 10.0, 100.0),
        Vectors.dense(2.0, 20.0, 200.0),
        Vectors.dense(5.0, 33.0, 366.0))
    )  // 注意每个向量为行而不是列
    // 计算 Pearson's 相关性的矩阵.输入"spearman" 调用 Spearman's 的计算方法.
    // 默认使用 Pearson's 方法.
    val correlMatrix: Matrix = Statistics.corr(data, "pearson")
    println(correlMatrix.toString)
  }
}
