//package sparkMllib.mllib_rdd.featureExtraction
//
//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.mllib.feature.{StandardScaler, StandardScalerModel}
//import org.apache.spark.mllib.linalg.Vectors
//import org.apache.spark.mllib.util.MLUtils
//import org.apache.spark.sql.SparkSession
//
//object StandardScaler {
//  /**
//    * 标准化是指：对于训练集中的样本，基于列统计信息将数据除以方差或（且）者将数据减去其均值（结果是方差等于1，数据在 0 附近）。这是很常用的预处理步骤。
//例如，当所有的特征具有值为 1 的方差且/或值为 0 的均值时，SVM 的径向基函数（RBF）核或者 L1 和 L2 正则化线性模型通常有更好的效果。
//标准化可以提升模型优化阶段的收敛速度，还可以避免方差很大的特征对模型训练产生过大的影响。
//    * @param args
//    */
//  def main(args: Array[String]): Unit = {
//    Logger.getLogger("org").setLevel(Level.ERROR)
//    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
//    // 加载文件（每行一个）
//    val data = MLUtils.loadLibSVMFile(spark.sparkContext, "data/mllib/sample_libsvm_data.txt")
//
//    import spark.implicits._
//    data.toDF().show()
////    withMean 默认值False. 在尺度变换（除方差）之前使用均值做居中处理（减去均值）。这会导致密集型输出，所以在稀疏数据上无效。
////    withStd 默认值True. 将数据缩放（尺度变换）到单位标准差。
//    val scaler1 = new StandardScaler().fit(data.map(x => x.features))
//    val scaler2 = new StandardScaler(withMean = true, withStd = true).fit(data.map(x => x.features))
//    // scaler3 is an identical model to scaler2, and will produce identical transformations
//    val scaler3 = new StandardScalerModel(scaler2.std, scaler2.mean)
//
//    // data1 will be unit variance.
//    val data1 = data.map(x => (x.label, scaler1.transform(x.features)))
//
//    // data2 will be unit variance and zero mean.
//    val data2 = data.map(x => (x.label, scaler2.transform(Vectors.dense(x.features.toArray))))
//  }
//}
