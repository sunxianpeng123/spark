package sparkMllib.mllib_rdd.EvaluationMetrics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

object MulticlassClassification {
  /**
    * Multiclass classification（多项分类）
    *
    * 多项分类描述了这样一种分类问题，对于每个数据点都有种可能的分类标签（当时为二项分类问题）。
    * 举例来说，区分手写样本为0到9的数字就是一种有10个可能分类的多项分类问题。
    * 对于多类别评估指标，积极和消极的概念跟二项分类略有不同。预测值和实际分类仍然可以是积极的或消极的，但是它们必须被归类到某种特定的类别中。
    * 每一个类别标签和预测值都会分配给多个类别中的一个，所以它对于分配的类别来说是积极的，但对于其他类别则是消极的。
    * 因此，true positive 发生在预测值和实际分类相吻合的时候，而 true negative 发生在预测值和实际分类都不属于一个类别的时候。
    * 由于这个认识，对于一个数据样本而言存在多个 true negative。根据之前对于 positive 和 negative 的定义扩展到 false negative 和 false positive 时就相对直接了。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    // 以 LIBSVM 格式加载训练数据
    val data = MLUtils.loadLibSVMFile(spark.sparkContext, "data/mllib/sample_multiclass_classification_data.txt")

    // 分割数据为训练集（60%）和测试集（40%）
    val Array(training, test) = data.randomSplit(Array(0.6, 0.4), seed = 11L)
    training.cache()

    // 运行训练算法来建模
    val model = new LogisticRegressionWithLBFGS()
      .setNumClasses(3)
      .run(training)

    // 使用测试集计算原始分数
    val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
      val prediction = model.predict(features)
      (prediction, label)
    }

    // 实例化指标对象
    val metrics = new MulticlassMetrics(predictionAndLabels)

    // 混淆矩阵
    println("Confusion matrix:")
    println(metrics.confusionMatrix)

    // 总体统计结果
    val accuracy = metrics.accuracy
    println("Summary Statistics")
    println(s"Accuracy = $accuracy")

    // 基于类别计算 Precision
    val labels = metrics.labels
    labels.foreach { l =>
      println(s"Precision($l) = " + metrics.precision(l))
    }

    // 基于类别计算 Recall
    labels.foreach { l =>
      println(s"Recall($l) = " + metrics.recall(l))
    }

    // 基于类别计算 False positive rate
    labels.foreach { l =>
      println(s"FPR($l) = " + metrics.falsePositiveRate(l))
    }

    // 基于类别计算 F-measure
    labels.foreach { l =>
      println(s"F1-Score($l) = " + metrics.fMeasure(l))
    }

    // 加权统计结果
    println(s"Weighted precision: ${metrics.weightedPrecision}")
    println(s"Weighted recall: ${metrics.weightedRecall}")
    println(s"Weighted F1 score: ${metrics.weightedFMeasure}")
    println(s"Weighted false positive rate: ${metrics.weightedFalsePositiveRate}")



  }
}
