package SparkTaskOptimize

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/8/22
  * \* Time: 10:06
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object test {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sparkConf=new SparkConf()
      .set("spark.yarn.executor.memoryOverhead","2048")
      .set("spark.storage.memoryFraction","0.5")
      .set("spark.core.connection.ack.wait.timeout","300")
      .set("spark.shuffle.file.buffer","320kb")
      .set("spark.shuffle.memoryFraction","0.3")
      .set("spark.shuffle.manager","sort")//spark.shuffle.manager：hash、sort、tungsten-sort
      .set("spark.shuffle.sort.bypassMergeThreshold","550")
      .set("spark.default.parallelism","500")
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
      .set("spark.locality.wait", "10")
      .set("spark.reducer.maxSizeInFligh","48")
    val sqlContext = SparkSession
      .builder
      .appName("RegressionMetricsExample")
      .master("local[*]")
      .config(sparkConf)
      .getOrCreate()
    val confMap=sqlContext.conf.getAll
    confMap.foreach(kv=>
    println(kv._1+"___"+kv._2))
    // Load the data stored in LIBSVM format as a DataFrame.
    val data = sqlContext.read.format("libsvm").load("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\data/mllib/sample_libsvm_data.txt")

    // Split the data into training and test sets (30% held out for testing)
    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3), seed = 1234L)
    // Train a NaiveBayes model.
    val model = new NaiveBayes()
      .fit(trainingData)
    // Select example rows to display.
    val predictions = model.transform(testData)
//    predictions.show()

    // Select (prediction, true label) and compute test error
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)
    println("Test set accuracy = " + accuracy)
  }
}
