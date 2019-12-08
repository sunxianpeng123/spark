package sparkMllib.mllib_rdd.DataTypes
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object labeledPoint {
  /**
    *1、标记点是与标签/响应相关联的局部矢量，密集或稀疏。
    * 在MLlib中，标注点用于监督学习算法。 我们使用双重存储标签，所以我们可以在回归和分类中使用标记点。
    * 对于二进制分类，标签应为0（负）或1（正）。 对于多类分类，标签应该是从零开始的类索引：0，1，2，....
    *
    * 2、稀疏数据
    * 在实践中, 有稀疏的训练数据是很常见的。MLlib 支持以 LIBSVM 格式存储的阅读培训示例, 这是  LIBSVM 和 LIBLINEAR 使用的默认格式。
    * 它是一种文本格式, 每个行都使用以下格式表示一个标记的稀疏特征向量:
    * label index1:value1 index2:value2 ...
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    // Create a labeled point with a positive label and a dense feature vector.
    val pos = LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0))

    // Create a labeled point with a negative label and a sparse feature vector.
    val neg = LabeledPoint(0.0, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)))
    println(pos)
    println(neg)

    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val examples: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(spark.sparkContext, "data/mllib/sample_libsvm_data.txt")
    examples.take(1).foreach(line=>{
      println(line.label)
      println(line.features)
    })
  }
}
