package sparkMllib.ml_df.FeatureTransformers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.{OneHotEncoder, StringIndexer}
import org.apache.spark.sql.SparkSession

object oneHotEncoder {
  /**
    * ​ 独热编码是指把一列标签索引映射成一列二进制数组，且最多的时候只有一位有效。这种编码适合一些期望类别特征为连续特征的算法，比如说逻辑斯蒂回归。
    * 对于离散特征，例如，性别：｛男，女｝，可以采用one-hot编码的方式将特征表示为一个m维向量，其中m为特征的取值个数。在one-hot向量中只有一个维度的值为1，其余为0。
    * 以“性别”这个特征为例，我们可以用向量 “1，0”表示“男”，向量 “0，1”表示“女”。使用one-hot编码可将离散特征的取值扩展到了欧式空间，便于进行相似度计算。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("BinarizerExample")
      .master("local[*]")
      .getOrCreate()
    val df = spark.createDataFrame(Seq(
      (0, "a"),
      (1, "b"),
      (2, "c"),
      (3, "a"),
      (4, "a"),
      (5, "c"),
      (6, "d"),
      (7, "d"),
      (8, "d"),
      (9, "d"),
      (10, "e"),
      (11, "e"),
      (12, "e"),
      (13, "e"),
      (14, "e")
    )).toDF("id", "category")

    val indexer = new StringIndexer()
      .setInputCol("category")
      .setOutputCol("categoryIndex")
      .fit(df)
    val indexed = indexer.transform(df)

    // IndexDouble => SparseVector
    // OneHotEncode:实际上是转换成了稀疏向量
    // Spark源码: The last category is not included by default 最后一个种类默认不包含
    // 和python scikit-learn's OneHotEncoder不同，scikit-learn's OneHotEncoder包含所有
    //OneHotEncoder，独热编码即 One-Hot 编码，又称一位有效编码，其方法是使用N位状态寄存器来对N个状态进行编码，每个状态都由他独立的寄存器位，并且在任意时候，其中只有一位有效。
    //可以这样理解，对于每一个特征，如果它有m个可能值，那么经过独热编码后，就变成了m个二元特征。并且，这些特征互斥，每次只有一个激活。因此，数据会变成稀疏的。
    val encoder = new OneHotEncoder()
      .setInputCol("categoryIndex")
      .setOutputCol("categoryVec")//SparseVector

    val encoded = encoder.transform(indexed)
    encoded.show()

    //整个categoryVec列就变成了一个稀疏矩阵。
    /**
      *  在上例中，我们构建了一个dataframe，包含“a”，“b”，“c”，“d”，“e” 五个标签，
      *  通过调用OneHotEncoder，我们发现出现频率最高的标签
      *  “e”被编码成第0位为1，即第0位有效，出现频率第二高的标签
      *  “d”被编码成第1位有效，
      *  依次类推，“a”和“c”也被相继编码，出现频率最小的标签“b”被编码成全0。
      */




  }
}
