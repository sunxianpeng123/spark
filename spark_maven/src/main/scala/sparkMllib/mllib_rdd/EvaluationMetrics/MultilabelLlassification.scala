package sparkMllib.mllib_rdd.EvaluationMetrics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.evaluation.MultilabelMetrics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object MultilabelLlassification {
  /**
    * Multilabel classification （多标签分类）
    * 多标签分类问题指的是将一个数据集中的每一个样本映射到一组分类标签中去。
    * 在这一类分类问题中，各个标签所包含的样本并不是互斥的。
    * 举例来说，将一组新闻稿分类到不同的主题中去，一篇稿子可能既属于科技类，又属于政治类。
    * 由于标签不是彼此互斥，预测值和真实的分类标签就成为了标签集合的向量，而非标签的向量。
    * 于是多标签评估指标就可以从基本的 precision、recall 等概念扩展到对集合的操作上。
    * 例如，对于某个类的 true positive 就发生在当这个类别存在于某个数据点的预测值集合中，并且也存在于它的真实类别集合中时。
    *
    * 以下代码片段阐释了如何去评估一个多标签分类器的效果。下面的多标签分类例子中使用了伪造的预测数据和标签数据。
    * 对于每一篇文章的预测结果为：
    * 文章 0 - 预测值为 0, 1 - 类别为 0, 2
    * 文章 1 - 预测值为 0, 2 - 类别为 0, 1
    * 文章 2 - 预测值为空 - 类别为 0
    * 文章 3 - 预测值为 2 - 类别为 2
    * 文章 4 - 预测值为 2, 0 - 类别为 2, 0
    * 文章 5 - 预测值为 0, 1, 2 - 类别为 0, 1
    * 文章 6 - 预测值为 1 - 类别为 1, 2
    * 对于每一类别的预测结果为：
    * 类别 0 - 文章 0, 1, 4, 5（一共 4 篇）
    * 类别 1 - 文章 0, 5, 6（一共 3 篇）
    * 类别 2 - 文章 1, 3, 4, 5（一共 4 篇）
    * 每一类别实际包含的文章为：
    * 类别 0 - 文章 0, 1, 2, 4, 5（一共 5 篇）
    * 类别 1 - 文章 1, 5, 6（一共 3 篇）
    * 类别 2 - 文章 0, 3, 4, 6（一共 4 篇）
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val scoreAndLabels: RDD[(Array[Double], Array[Double])] = spark.sparkContext.parallelize(
      Seq((Array(0.0, 1.0), Array(0.0, 2.0)),
        (Array(0.0, 2.0), Array(0.0, 1.0)),
        (Array.empty[Double], Array(0.0)),
        (Array(2.0), Array(2.0)),
        (Array(2.0, 0.0), Array(2.0, 0.0)),
        (Array(0.0, 1.0, 2.0), Array(0.0, 1.0)),
        (Array(1.0), Array(1.0, 2.0))), 2)

    // 实例化指标对象
    val metrics = new MultilabelMetrics(scoreAndLabels)

    // 总体统计结果
    println(s"Recall = ${metrics.recall}")
    println(s"Precision = ${metrics.precision}")
    println(s"F1 measure = ${metrics.f1Measure}")
    println(s"Accuracy = ${metrics.accuracy}")

    // 基于每一标签的统计结果
    metrics.labels.foreach(label =>
      println(s"Class $label precision = ${metrics.precision(label)}"))
    metrics.labels.foreach(label => println(s"Class $label recall = ${metrics.recall(label)}"))
    metrics.labels.foreach(label => println(s"Class $label F1-score = ${metrics.f1Measure(label)}"))

    // Micro stats
    println(s"Micro recall = ${metrics.microRecall}")
    println(s"Micro precision = ${metrics.microPrecision}")
    println(s"Micro F1 measure = ${metrics.microF1Measure}")

    // Hamming loss
    println(s"Hamming loss = ${metrics.hammingLoss}")

    // Subset accuracy
    println(s"Subset accuracy = ${metrics.subsetAccuracy}")
  }
}
