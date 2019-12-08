package sparkMllib.mllib_rdd.PMMLModelExport

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

object PMML {
  /**
    * Spark.mllib支持模型导出到Predictive Model Markup Language（预测模型标记语言）。
    * 下表列出了可以导出到PMML的spark.mllib模型及其等效的PMML模型。
    * spark.mllib Model       PMML model
    * KMeansModel           	ClusteringModel
    * LinearRegressionModel	  RegressionModel (functionName="regression")
    * RidgeRegressionModel	  RegressionModel (functionName="regression")
    * LassoModel	            RegressionModel (functionName="regression")
    * SVMModel	              RegressionModel (functionName="classification" normalizationMethod="none")
    * Binary LogisticRegressionModel	  RegressionModel (functionName="classification" normalizationMethod="logit")
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    // 加载并解析数据
    val data = spark.sparkContext.textFile("data/mllib/kmeans_data.txt")
    val parsedData = data.map(s =>Vectors.dense(s.split(' ').map(_.toDouble))).cache()

    // 使用KMeans将数据分成两类
    val numClusters = 2
    val numIterations = 20
    val clusters =KMeans.train(parsedData, numClusters, numIterations)

    // 以PMML格式导出一个字符串
    println("PMML Model:\n" + clusters.toPMML)
    // 将模型导出为PMML格式的本地文件
    clusters.toPMML("/tmp/kmeans.xml")
    // 以PMML格式将模型导出到分布式文件系统的目录上
    clusters.toPMML(spark.sparkContext, "/tmp/kmeans")
    // 将模型导出为PMML格式的输出流
    clusters.toPMML(System.out)
  }
}
