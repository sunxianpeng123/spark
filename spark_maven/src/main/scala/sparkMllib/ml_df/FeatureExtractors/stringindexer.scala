package sparkMllib.ml_df.FeatureExtractors
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.sql.SparkSession

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/3
  * \* Time: 14:59
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object stringindexer {
  def main(args: Array[String]): Unit = {
  //StringIndexer（字符串-索引变换）将标签的字符串列编号变成标签索引列。标签索引序列的取值范围是[0，numLabels（字符串中所
    // 有出现的单词去掉重复的词后的总和）]，按照标签出现频率排序，出现最多的标签索引为0。如果输入是数值型，我们先将数值
    // 映射到字符串，再对字符串进行索引化。如果下游的 pipeline（例如：Estimator 或者 Transformer）需要用到索引化后的标签
    // 序列，则需要将这个 pipeline 的输入列名字指定为索引化序列的名字。大部分情况下，通过 setInputCol 设置输入的列名。
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val df = spark.createDataFrame(
      Seq((0, "a"), (1, "b"), (2, "c"), (3, "a"), (4, "a"), (5, "c"))
    ).toDF("id", "category")

    val indexer = new StringIndexer()
      .setInputCol("category")
      .setOutputCol("categoryIndex")

    val indexed = indexer.fit(df).transform(df)
    df.show()
    indexed.show()

  }
}

