package sparkMllib.mllib_rdd.DimensionalityReduction

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.{Matrix, SingularValueDecomposition, Vector, Vectors}
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.sql.SparkSession

object svd {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val data = Array(
      Vectors.sparse(5, Seq((1, 1.0), (3, 7.0))),
      Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0),
      Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0))
    val dataRDD = spark.sparkContext.parallelize(data, 2)
    val mat: RowMatrix = new RowMatrix(dataRDD)
    // 计算最明显的5个奇异值和相应的奇异变量
    val svd: SingularValueDecomposition[RowMatrix, Matrix] =  mat.computeSVD(5, computeU = true)
    val U: RowMatrix = svd.U  // U是一个RowMatrix类.
    val s: Vector = svd.s  // 奇异值存储在局部密集矢量中.
    val V: Matrix = svd.V  // V是一个局部密集矩阵.
    println(U.rows)
    println("**")
    println(s)
    println("**")
    println(V)
  }
}
