package sparkMllib.mllib_rdd.DataTypes

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.{Matrices, Matrix}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object LocalMatrix {
  /**
    * 1、本地矩阵具有整数类型的行和列索引和双类型值，存储在单个机器上。 MLlib支持密集矩阵，其入口值以列主序列存储在单个双阵列中，稀疏矩阵的非零入口值以列主要顺序存储在压缩稀疏列（CSC）格式中。
    * 例如，以下密集矩阵
    * (1.0 , 2.0)
    * (3.0 , 4.0)
    * (5.0 , 6.0)
    * 存储在具有矩阵大小  (3, 2) 的一维数组  [1.0, 3.0, 5.0, 2.0, 4.0, 6.0]  中。
    *
    * 2、局部矩阵的基类是Matrix，
    * 我们提供了两个实现： DenseMatrix 和 SparseMatrix。
    * 我们建议使用 Matrices 中实现的工厂方法来创建本地矩阵。
    * 记住，MLlib中的局部矩阵以列主要顺序存储。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val examples: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(spark.sparkContext, "data/mllib/sample_libsvm_data.txt")
    // Create a dense matrix ((1.0, 2.0), (3.0, 4.0), (5.0, 6.0))
    val dm: Matrix = Matrices.dense(3, 2, Array(1.0, 3.0, 5.0, 2.0, 4.0, 6.0))

    // Create a sparse matrix ((9.0, 0.0), (0.0, 8.0), (0.0, 6.0))
    //values=Array(9, 6, 8)
//    rowIndexs= Array(0, 2, 1)表示非零元素所在的行索引
//    colPtrs=Array(0, 1, 3)=> 每列非0个数（1-0，3-1）=（1,2），表示第一列有一个非零元素，第二列有两个非零元素
    val sm: Matrix = Matrices.sparse(3, 2, Array(0, 1, 3), Array(0, 2, 1), Array(9, 6, 8))

    println(sm)


  }
}
