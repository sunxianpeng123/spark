package sparkMllib.mllib_rdd.Optimization

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.classification.LogisticRegressionModel
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.optimization.{LBFGS, LogisticGradient, SquaredL2Updater}
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

object L_BFGS {
  /**
    * L-BFGS目前只是MLlib中的一个低级优化原语。
    * 如果要在各种ML算法（如线性回归和逻辑回归）中使用L-BFGS，则必须将目标函数的渐变和更新器传递给优化器，而不是使用诸如LogisticRegressionWithSGD之类的培训API。
    * 参见下面的例子。
    * 将在下一个版本中解决。使用L1Updater的L1正则化将不起作用，因为L1Updater中的软阈值逻辑被设计用于梯度下降。 请参阅开发人员的说明。
    * L-BFGS方法LBFGS.runLBFGS具有以下参数：
    * Gradient是一个类，它以当前参数值计算正在优化的目标函数的梯度，即相对于单个训练示例。 MLlib包括常见损失函数的梯度类，例如铰链，后勤，最小二乘法。梯度类将训练样本，其标签和当前参数值作为输入。
    * 更新器是一种计算L-BFGS正则化部分的目标函数的梯度和损耗的类。 MLlib包括没有正则化的情况的更新程序，以及L2正则化程序。
    * numCorrections是L-BFGS更新中使用的更正次数。建议10。
    * maxNumIterations是L-BFGS可以运行的最大迭代次数。
    * regParam是使用正则化时的正则化参数。
    * converTol控制当L-BFGS被认为收敛时仍然允许多少相对变化。这必须是非负的。较低的值较不容忍，因此通常会导致更多的迭代运行。该值考察了Breeze LBFGS中的平均改善度和梯度范数。
    * 返回是包含两个元素的元组。 第一个元素是包含每个要素权重的列矩阵，第二个元素是包含为每次迭代计算的损失的数组。
    * 这是使用L-BFGS优化器来训练二元逻辑回归与L2正则化的一个例子。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val data = MLUtils.loadLibSVMFile(spark.sparkContext, "data/mllib/sample_libsvm_data.txt")
    val numFeatures = data.take(1)(0).features.size

    // Split data into training (60%) and test (40%).
    val splits = data.randomSplit(Array(0.6, 0.4), seed = 11L)

    // Append 1 into the training data as intercept.
    val training = splits(0).map(x => (x.label, MLUtils.appendBias(x.features))).cache()

    val test = splits(1)

    // Run training algorithm to build the model
    val numCorrections = 10
    val convergenceTol = 1e-4
    val maxNumIterations = 20
    val regParam = 0.1
    val initialWeightsWithIntercept = Vectors.dense(new Array[Double](numFeatures + 1))

    val (weightsWithIntercept, loss) = LBFGS.runLBFGS(
      training,
      new LogisticGradient(),
      new SquaredL2Updater(),
      numCorrections,
      convergenceTol,
      maxNumIterations,
      regParam,
      initialWeightsWithIntercept)

    val model = new LogisticRegressionModel(
      Vectors.dense(weightsWithIntercept.toArray.slice(0, weightsWithIntercept.size - 1)),
      weightsWithIntercept(weightsWithIntercept.size - 1))

    // Clear the default threshold.
    model.clearThreshold()

    // Compute raw scores on the test set.
    val scoreAndLabels = test.map { point =>
      val score = model.predict(point.features)
      (score, point.label)
    }

    // Get evaluation metrics.
    val metrics = new BinaryClassificationMetrics(scoreAndLabels)
    val auROC = metrics.areaUnderROC()

    println("Loss of each step in training process")
    loss.foreach(println)
    println("Area under ROC = " + auROC)
  }
}
