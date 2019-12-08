package sparkMllib.ml_df.FeatureTransformers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.attribute.Attribute
import org.apache.spark.ml.feature.{IndexToString, StringIndexer}
import org.apache.spark.sql.SparkSession

object stringIndexerString {
  /**
    *1、 StringIndexer（字符串-索引变换）
    * 将标签的字符串列编号变成标签索引列。标签索引序列的取值范围是[0，numLabels（字符串中所有出现的单词去掉重复的词后的总和）]，
    * 按照标签出现频率排序，出现最多的标签索引为0。如果输入是数值型，我们先将数值映射到字符串，再对字符串进行索引化。
    * 如果下游的 pipeline（例如：Estimator 或者 Transformer）需要用到索引化后的标签序列，则需要将这个 pipeline 的输入列名字指定为索引化序列的名字。
    * 大部分情况下，通过 setInputCol 设置输入的列名。
    *   （1）另外，StringIndexer 在转换新数据时提供两种容错机制处理训练中没有出现的标签
    *         StringIndexer 抛出异常错误（默认值）
    *         跳过未出现的标签实例。
    *         如果没有在 StringIndexer 里面设置未训练过（unseen）的标签的处理或者设置未 “error”，运行时会遇到程序抛出异常。当然，也可以通过设置 setHandleInvalid("skip")
    *
    * 2、IndexToString（索引-字符串变换）
    * 与 StringIndexer 对应，IndexToString 将索引化标签还原成原始字符串。
    * 一个常用的场景是先通过 StringIndexer 产生索引化标签，然后使用索引化标签进行训练，最后再对预测结果使用 IndexToString来获取其原始的标签字符串。
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
      Seq((0, "a"), (1, "b"), (2, "c"), (3, "a"), (4, "a"), (5, "c"))
    ).toDF("id", "category")

    val indexer = new StringIndexer()
      .setInputCol("category")
      .setOutputCol("categoryIndex")
      .setHandleInvalid("skip")

    val indexed = indexer.fit(df).transform(df)
    println(s"Transformed string column '${indexer.getInputCol}' " +
      s"to indexed column '${indexer.getOutputCol}'")
    indexed.show()
    val inputColSchema = indexed.schema(indexer.getOutputCol)
    println(s"StringIndexer will store labels in output column metadata: " +
      s"${Attribute.fromStructField(inputColSchema).toString}\n")

    val converter = new IndexToString()
      .setInputCol("categoryIndex")
      .setOutputCol("originalCategory")

    val converted = converter.transform(indexed)
    println(s"Transformed indexed column '${converter.getInputCol}' back to original string " +
      s"column '${converter.getOutputCol}' using labels in metadata")
    converted.show()
  }
}
