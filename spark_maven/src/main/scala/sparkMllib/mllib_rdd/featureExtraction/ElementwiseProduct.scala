//package sparkMllib.mllib_rdd.featureExtraction
//
//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.mllib.feature.ElementwiseProduct
//import org.apache.spark.mllib.linalg.Vectors
//import org.apache.spark.sql.SparkSession
//
//object ElementwiseProduct {
//  /**
//    *ElementwiseProduct 对输入向量的每个元素乘以一个给定的权重向量的每个元素，对输入向量每个元素逐个进行放缩。
//    * 这个称为对输入向量 v 和变换向量 scalingVec 使用 Hadamard product (阿达玛积)进行变换，最终产生一个新的向量。Qu8T948*1#  用向量 w 表示 scalingVec ，则 Hadamard product 可以表示为
//    *
//    * @param args
//    */
//  def main(args: Array[String]): Unit = {
//    Logger.getLogger("org").setLevel(Level.ERROR)
//    val spark = SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
//    // Create some vector data; also works for sparse vectors
//    val data = spark.sparkContext.parallelize(Array(Vectors.dense(1.0, 2.0, 3.0), Vectors.dense(4.0, 5.0, 6.0)))
//    import spark.implicits._
//    data.foreach(println)
//    println(data.count())
//
//
//    val transformingVector = Vectors.dense(0.0, 1.0, 2.0)
//    println(transformingVector)
//    val transformer = new ElementwiseProduct(transformingVector)
//
//    // Batch transform and per-row transform give the same results:
//    val transformedData = transformer.transform(data)
//    transformedData.foreach(println)
//    println("****")
//    val transformedData2 = data.map(x => transformer.transform(x))
//    println(transformedData2.foreach(println))
//
//
//
//  }
//}
