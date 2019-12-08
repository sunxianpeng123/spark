package sparkMllib.mllib_rdd.DataTypes

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.distributed._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.linalg.{Vector, Vectors}
object DistributedMatrix {
  /**
    * 分布式矩阵具有长类型的行和列索引和双类型值，分布式存储在一个或多个RDD中。
    * 选择正确的格式来存储大型和分布式矩阵是非常重要的。将分布式矩阵转换为不同的格式可能需要全局shuffle，这是相当昂贵的。
    * 到目前为止已经实现了四种类型的分布式矩阵。
    * 基本类型称为RowMatrix。
    * 1、RowMatrix是没有有意义的行索引的行向分布式矩阵，例如特征向量的集合。它由其行的RDD支持，其中每行是局部向量。
    * 我们假设RowMatrix的列数不是很大，因此单个本地向量可以合理地传递给驱动程序，也可以使用单个节点进行存储/操作。
    * ## RowMatrix是一个面向行的分布式矩阵，没有有意义的行索引，由其行的RDD支持，其中每一行都是一个局部向量。 由于每行由局部向量表示，所以列数受到整数范围的限制，但实际应该要小得多。
    * 2、IndexedRowMatrix与RowMatrix类似，但具有行索引，可用于标识行和执行连接。
    * 3、CoordinateMatrix是以坐标 list(COO) 格式存储的分布式矩阵，由其条目的RDD支持。
    *## CoordinateMatrix 是由其条目的RDD支持的分布式矩阵。 每个条目是 (i: Long, j: Long, value: Double) 的元组，其中i是行索引，j是列索引，value是条目值。 只有当矩阵的两个维度都很大并且矩阵非常稀疏时才应使用Coordinate矩阵。
    * 4、BlockMatrix是由MatrixBlock的RDD支持的分布式矩阵，它是（Int，Int，Matrix）的元组。
    *##  BlockMatrix是由MatrixBlocks的RDD支持的分布式矩阵，其中MatrixBlock是  ((Int, Int), Matrix) 的元组，其中 (Int, Int)  是块的索引，Matrix是子矩阵，
    * 在给定索引处的矩阵大小为rowsPerBlock x colsPerBlock。 BlockMatrix支持诸如添加和乘以另一个BlockMatrix的方法。 BlockMatrix还有一个帮助函数validate，可用于检查BlockMatrix是否正确设置。
    * 5、Note
    * 分布式矩阵的底层RDD必须是确定性的，因为我们缓存矩阵大小。一般来说，使用非确定性RDD可能会导致错误。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val rows: RDD[Vector] =spark.sparkContext.textFile("data/mllib/kmeans_data.txt").map{line=>{
      Vectors.dense(line.split(" ").map(ele=>{
        ele.toDouble
      }))
    }} // an RDD of local vectors
    rows.foreach(println)
    println("============1、RowMatrix=============")
    //1、RowMatrix
    // Create a RowMatrix from an RDD[Vector].
    val mat: RowMatrix = new RowMatrix(rows)
    // Get its size.'
    val m = mat.numRows()
    val n = mat.numCols()
    // QR decomposition
    val qrResult = mat.tallSkinnyQR(true)// QR分解形式为A = QR，其中Q是正交矩阵，R是上三角矩阵。
    println(m)
    println(n)
    //println(qrResult)
    mat.rows.foreach(println)
    println("=============2、IndexedRowMatrix==============")
    //2、IndexedRowMatrix
    val rows1: RDD[IndexedRow] = spark.sparkContext.textFile("data/mllib/kmeans_data.txt")
      .map(_.split(" ").map(_.toDouble))
      .map(line=>{Vectors.dense(line)})
      .map((vd)=>new IndexedRow(vd.size,vd))
    rows1.foreach(println)
    // an RDD of indexed rows
    // Create an IndexedRowMatrix from an RDD[IndexedRow].
    val mat1: IndexedRowMatrix = new IndexedRowMatrix(rows1)
    // Get its size.
    val m1 = mat1.numRows()
    val n1= mat1.numCols()
    // Drop its row indices.
    val rowMat1: RowMatrix = mat1.toRowMatrix()
    println(m1)
    println(n1)
    mat1.rows.foreach(println)
    println("============3、CoordinateMatrix=============")
    //3、CoordinateMatrix
    val entries: RDD[MatrixEntry] =spark.sparkContext.textFile("data/mllib/kmeans_data.txt")
      .map(_.split(' ') //按“ ”分割
      .map(_.toDouble)) //转成Double类型
      .map(vue => (vue(0).toLong, vue(1).toLong, vue(2))) //转化成坐标格式
      .map(vue2 => new MatrixEntry(vue2 _1, vue2 _2, vue2 _3)) //转化成坐标矩阵格式 // an RDD of matrix entries
    // Create a CoordinateMatrix from an RDD[MatrixEntry].
    val mat2: CoordinateMatrix = new CoordinateMatrix(entries)
    // Get its size.
    val m2 = mat2.numRows()
    val n2 = mat2.numCols()

    // Convert it to an IndexRowMatrix whose rows are sparse vectors.
    val indexedRowMatrix = mat2.toIndexedRowMatrix()

    mat2.entries.foreach(println) //打印数据
    println(mat2.numCols())
    println(mat2.numCols())
    //    Return approximate number of distinct elements in the RDD.
    println(mat2.entries.countApproxDistinct())
    println("=============4、BlockMatrix===============")
    //4、BlockMatrix
    val entries1: RDD[MatrixEntry] =spark.sparkContext.textFile("data/mllib/kmeans_data.txt")
      .map(_.split(' ') //按“ ”分割
        .map(_.toDouble))
      .map(vue => (vue(0).toLong, vue(1).toLong, vue(2))) //转化成坐标格式
      .map(vue2 => new MatrixEntry(vue2 _1, vue2 _2, vue2 _3)) //转化成坐标矩阵格式 // an RDD of matrix entries//转成Double类型// an RDD of (i, j, v) matrix entries
    // Create a CoordinateMatrix from an RDD[MatrixEntry].
    val coordMat: CoordinateMatrix = new CoordinateMatrix(entries1)
    // Transform the CoordinateMatrix to a BlockMatrix
    val matA: BlockMatrix = coordMat.toBlockMatrix().cache()//toBlockMatrix默认创建大小为1024 x 1024的块。 用户可以通过toBlockMatrix（rowsPerBlock，colsPerBlock）提供值来更改块大小。
    // Validate whether the BlockMatrix is set up properly. Throws an Exception when it is not valid.
    matA.blocks.foreach(tup=>{
      val x =tup._1._1
      val y=tup._1._2
      val matb =tup._2
      println("#################")
      println(x+"___"+y)
      println(matb)
      println("***")
      matb.toArray.foreach(println)

    })
    // Nothing happens if it is valid.
    matA.validate()

    // Calculate A^T A.
    val ata = matA.transpose.multiply(matA)

  }
}
