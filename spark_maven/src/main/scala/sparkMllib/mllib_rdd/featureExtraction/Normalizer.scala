//package sparkMllib.mllib_rdd.featureExtraction
//
//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.mllib.feature.Normalizer
//import org.apache.spark.mllib.util.MLUtils
//import org.apache.spark.sql.SparkSession
//
//object Normalizer {
//  /**
//    * 归一化是指将每个独立样本做尺度变换从而是该样本具有单位  Lp 范数。这是文本分类和聚类中的常用操作。例如，两个做了L2归一化的TF-IDF向量的点积是这两个向量的cosine（余弦）相似度。
//    * 简化计算，缩小量值的办法
//    * 1、把数变为（0，1）之间的小数
//    * 2、把有量纲表达式变为无量纲表达式
//    */
//  def main(args: Array[String]): Unit = {
//    Logger.getLogger("org").setLevel(Level.ERROR)
//    val spark = SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
//
//    val data = MLUtils.loadLibSVMFile(spark.sparkContext, "data/mllib/sample_libsvm_data.txt")
//
//    //Normalizer 的构造函数有以下参数：在Lp空间的p范数, 默认 p=2。
//    val normalizer1 = new Normalizer()//使用 L2 范数
//    val normalizer2 = new Normalizer(p = Double.PositiveInfinity)// 使用L∞ 范数归一化。
//
//    // Each sample in data1 will be normalized using $L^2$ norm.
//    val data1 = data.map(x => (x.label, normalizer1.transform(x.features)))
//
//    // Each sample in data2 will be normalized using $L^\infty$ norm.
//    val data2 = data.map(x => (x.label, normalizer2.transform(x.features)))
//
//  }
//}
