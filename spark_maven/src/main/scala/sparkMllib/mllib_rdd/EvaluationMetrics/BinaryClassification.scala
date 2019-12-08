package sparkMllib.mllib_rdd.EvaluationMetrics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

object BinaryClassification {
  /**
    * 二项分类器可以用来区分数据集中的元素到两个可能的组中（如欺诈和非欺诈），而且它是多项分类器的一种特殊情况。多数的二项分类评估指标可以被推广使用到多项分类评估中。
    * Threshold tuning（阈值调优）
    * 很多分类模型对于每一个类实际上会输出一个“分数”（经常是一个概率值），其中较高的分数表示更高的可能性，理解这一点非常重要。
    * 在二项分类场景中，模型会为每个类别输出一个概率： 和 。并不是所有场景都会取更高概率的类别，
    * 有些场景下模型可能需要进行调整来使得它只有在某个概率非常高的情况下才去预测这个类别（如只有在模型预测的欺诈概率高于90%的情况下才屏蔽这笔信用卡交易）。
    * 所以，根据模型输出的概率来确定预测的类别是由预测的阈值来决定的。
    * 调整预测阈值将会改变模型的 precision 和 recall，而且它是模型调优的重要环节。
    * 为了能够将 precision、recall 和其他评估指标根据阈值的改变是如何变化的这一情况可视化出来，一种常见的做法是通过阈值的参数化来绘制相互作对比的指标。
    * 一种图叫做 P-R 曲线，它会将 (precision, recall) 组成的点根据不同的阈值绘制出来，而另一种叫做 receiver operating characteristic 或者 ROC 曲线，它会绘制出 (recall, false positive rate) 组成的点。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    // 以 LIBSVM 格式加载训练数据
    val data = MLUtils.loadLibSVMFile(spark.sparkContext, "data/mllib/sample_binary_classification_data.txt")

    // 分割数据为训练集（60%）和测试集（40%）
    val Array(training, test) = data.randomSplit(Array(0.6, 0.4), seed = 11L)
    training.cache()

    // 运行训练算法来建模
    val model = new LogisticRegressionWithLBFGS()
      .setNumClasses(2)
      .run(training)

    // 清空预测阈值来使模型返回概率值
    model.clearThreshold

    // 使用测试集计算原始分数
    val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
      val prediction = model.predict(features)
      (prediction, label)
    }

    // 实例化指标对象
    val metrics = new BinaryClassificationMetrics(predictionAndLabels)

    // 按阈值分别求 precision
    val precision = metrics.precisionByThreshold
    precision.foreach { case (t, p) =>
      println(s"Threshold: $t, Precision: $p")
    }

    // 按阈值分别求 recall
    val recall = metrics.recallByThreshold
    recall.foreach { case (t, r) =>
      println(s"Threshold: $t, Recall: $r")
    }

    // Precision-Recall 曲线
    val PRC = metrics.pr

    // F-measure
    val f1Score = metrics.fMeasureByThreshold
    f1Score.foreach { case (t, f) =>
      println(s"Threshold: $t, F-score: $f, Beta = 1")
    }

    val beta = 0.5
    val fScore = metrics.fMeasureByThreshold(beta)
    f1Score.foreach { case (t, f) =>
      println(s"Threshold: $t, F-score: $f, Beta = 0.5")
    }

    // AUPRC
    val auPRC = metrics.areaUnderPR
    println("Area under precision-recall curve = " + auPRC)

    // 计算 ROC 和 PR 曲线中使用的阈值
    val thresholds = precision.map(_._1)

    // ROC 曲线
    val roc = metrics.roc

    // AUROC
    val auROC = metrics.areaUnderROC
    println("Area under ROC = " + auROC)
  }
}
