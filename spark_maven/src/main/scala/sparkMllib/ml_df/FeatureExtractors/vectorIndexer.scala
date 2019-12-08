package sparkMllib.ml_df.FeatureExtractors

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/3
  * \* Time: 15:22
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object vectorIndexer {
  //VectorIndexer可以帮助指定向量数据集中的分类特征。它可以自动确定哪些功能是分类的，并将原始值转换为类别索引。具体来说，它执行以下操作：
//  取一个Vector类型的输入列和一个参数maxCategories。
//  根据不同值的数量确定哪些功能应分类，其中最多maxCategories的功能被声明为分类。
//  为每个分类功能计算基于0的类别索引。
//  索引分类特征并将原始特征值转换为索引。
//  索引分类功能允许诸如决策树和树组合之类的算法适当地处理分类特征，提高性能。
//  在下面的示例中，我们读取标注点的数据集，然后使用VectorIndexer来确定哪些功能应被视为分类。我们将分类特征值转换为其索引。然后，该转换的数据可以传递给诸如DecisionTreeRegressor之类的算法来处理分类特征。
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()


  import org.apache.spark.ml.feature.VectorIndexer

  val data = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")

  val indexer = new VectorIndexer()
    .setInputCol("features")
    .setOutputCol("indexed")
    .setMaxCategories(10)

  val indexerModel = indexer.fit(data)

  val categoricalFeatures: Set[Int] = indexerModel.categoryMaps.keys.toSet
  println(s"Chose ${categoricalFeatures.size} categorical features: " +
    categoricalFeatures.mkString(", "))

  // Create new column "indexed" with categorical values transformed to indices
  val indexedData = indexerModel.transform(data)
  indexedData.show()

  }
}

