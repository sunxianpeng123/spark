//package sparkMllib.mllib_rdd.featureExtraction
//
//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.mllib.feature.PCA
//import org.apache.spark.mllib.linalg.Vectors
//import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
//import org.apache.spark.sql.SparkSession
//
//object PCA {
//  def main(args: Array[String]): Unit = {
//    Logger.getLogger("org").setLevel(Level.ERROR)
//    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
//    // 加载文件（每行一个）
//    val data = spark.sparkContext.textFile("data/mllib/ridge-data/lpsa.data").map { line =>
//      val parts = line.split(',')
//      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))//label-features
//    }.cache()
//
//    val splits = data.randomSplit(Array(0.6, 0.4), seed = 11L)
//    val training = splits(0).cache()
//    val test = splits(1)
//
//    println(training.first().features.size)
//
//      val pca = new PCA(k = training.first().features.size / 2).fit(data.map(_.features))//k 保留维数
//    //val pca = new PCA(training.first().features.size / 2)
//    //得到经过pca处理后的training和test矩阵
//    val training_pca = training.map(p => p.copy(features = pca.transform(p.features)))
//    val test_pca = test.map(p => p.copy(features = pca.transform(p.features)))
//
//    val numIterations = 100
//    //对使用pca处理前后的数据分别建立模型
//    val model = LinearRegressionWithSGD.train(training, numIterations)
//    val model_pca = LinearRegressionWithSGD.train(training_pca, numIterations)
//
//    //pca处理前后的预测结果
//    val valuesAndPreds = test.map { point =>
//      val score = model.predict(point.features)
//      (score, point.label)
//    }
//
//    val valuesAndPreds_pca = test_pca.map { point =>
//      val score = model_pca.predict(point.features)
//      (score, point.label)
//    }
//    //pca处理前后的均方差
//    val MSE = valuesAndPreds.map { case (v, p) => math.pow((v - p), 2) }.mean()//pow() 方法可返回 x 的 y 次幂的值。
//    val MSE_pca = valuesAndPreds_pca.map { case (v, p) => math.pow((v - p), 2) }.mean()
//
//    println("Mean Squared Error = " + MSE)
//    println("PCA Mean Squared Error = " + MSE_pca)
//  }
//}
