package sparkMllib.ml_df.FeatureTransformers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.SQLTransformer
import org.apache.spark.sql.SparkSession

object SQLTransformer {
  /**
    * SQLTransformer（SQL转换器）实现由 SQL 语句定义的转换 。目前我们只支持SQL语法如 "SELECT ... FROM __THIS__ ..." ，其中 "__THIS__" 代表输入数据集的基础表。选择语句指定输出中展示的字段、元素和表达式，支持Spark SQL 中的所有选择语句。用户还可以使用 Spark SQL 内置函数和U DFs（自定义函数）来对这些选定的列进行操作。SQLTransformer 支持如下语句：
    * SELECT a, a + b AS a_b FROM __THIS__
    * SELECT a, SQRT(b) AS b_sqrt FROM __THIS__ where a > 5
    * SELECT a, b, SUM(c) AS c_sum FROM __THIS__ GROUP BY a, b
    * 例子:
    * 假设我们有如下DataFrame包含 id，v1，v2列：
            id |  v1 |  v2
            ----|-----|-----
             0  | 1.0 | 3.0
             2  | 2.0 | 5.0
  下面是使用  "SELECT *, (v1 + v2) AS v3, (v1 * v2) AS v4 FROM __THIS__" 的 SQLTransformer 的输出：
        id |  v1 |  v2 |  v3 |  v4
        ----|-----|-----|-----|-----
         0  | 1.0 | 3.0 | 4.0 | 3.0
         2  | 2.0 | 5.0 | 7.0 |10.0
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("BinarizerExample")
      .master("local[*]")
      .getOrCreate()
    val df = spark.createDataFrame(
      Seq((0, 1.0, 3.0), (2, 2.0, 5.0))).toDF("id", "v1", "v2")

    val sqlTrans = new SQLTransformer().setStatement(
      "SELECT *, (v1 + v2) AS v3, (v1 * v2) AS v4 FROM __THIS__")

    sqlTrans.transform(df).show()
  }
}
