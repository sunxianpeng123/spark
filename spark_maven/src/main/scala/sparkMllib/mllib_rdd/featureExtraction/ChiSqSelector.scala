//package sparkMllib.mllib_rdd.featureExtraction
//
//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.mllib.feature.ChiSqSelector
//import org.apache.spark.mllib.linalg.Vectors
//import org.apache.spark.mllib.regression.LabeledPoint
//import org.apache.spark.mllib.util.MLUtils
//import org.apache.spark.sql.SparkSession
//
//object ChiSqSelector {
//  /**
//    * ChiSqSelector是指使用卡方（Chi-Squared）做特征选择。该方法操作的是有标签的类别型数据。ChiSqSelector基于卡方检验来排序数据，然后选出卡方值较大(也就是跟标签最相关)的特征（topk)。
//    * 卡方检验可以判断两个变量之间是否有显著的相关性（也可以说成是否有显著的独立性）
//   */
//  def main(args: Array[String]): Unit = {
//    Logger.getLogger("org").setLevel(Level.ERROR)
//    val spark = SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
//
//    // Load some data in libsvm format
//    val data = MLUtils.loadLibSVMFile(spark.sparkContext, "data/mllib/sample_libsvm_data.txt")
//    // Discretize data in 16 equal bins since ChiSqSelector requires categorical features
//    // Even though features are doubles, the ChiSqSelector treats each unique value as a category
//    val discretizedData = data.map { lp =>
//      LabeledPoint(lp.label, Vectors.dense(lp.features.toArray.map { x => (x / 16).floor }))
//    }
//    // Create ChiSqSelector that will select top 50 of 692 features
//    val selector = new ChiSqSelector(numTopFeatures = 50)//numTopFeatures 保留的卡方较大的特征的数量。
//    // Create ChiSqSelector model (selecting features)
//    val transformer = selector.fit(discretizedData)
//    // Filter the top 50 features from each feature vector
//    val filteredData = discretizedData.map { lp =>
//      LabeledPoint(lp.label, transformer.transform(lp.features))
//    }
//
//
//    println(filteredData.take(1)(0).features.size)
//  }
//}
