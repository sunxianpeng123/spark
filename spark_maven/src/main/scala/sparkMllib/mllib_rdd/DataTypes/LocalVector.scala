package sparkMllib.mllib_rdd.DataTypes

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.{Vectors,Vector}
import org.apache.spark.sql.SparkSession

object LocalVector {
  /**
    *1、 局部向量具有整数类型和基于0的索引和双类型值，存储在单个机器上。
    *     MLlib支持两种类型的局部向量：密集和稀疏。
    *     密集向量由表示其条目值的双数组支持，而稀疏向量由两个并行数组支持：索引和值。 例如，向量（1.0,0.0,3.0）可以密集格式表示为1.0,0.0,3.0，或以稀疏格式表示为（3，02，1.03.0），其中3是 矢量的大小。
      2、本地向量的基类是Vector，我们提供了两个实现：DenseVector 和 SparseVector。 我们建议使用 Vectors 中实现的工厂方法来创建本地向量。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    // Create a dense vector (1.0, 0.0, 3.0).
    val dv: Vector = Vectors.dense(1.0, 0.0, 3.0)
    // Create a sparse vector (1.0, 0.0, 3.0) by specifying its indices and values corresponding to nonzero entries.
    val sv1: Vector = Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0))
    // Create a sparse vector (1.0, 0.0, 3.0) by specifying its nonzero entries.
    val sv2: Vector = Vectors.sparse(3, Seq((0, 1.0), (2, 3.0)))
    println(dv)
    println(sv1)
    println(sv2)
    println(sv2.toDense)
  }
}
