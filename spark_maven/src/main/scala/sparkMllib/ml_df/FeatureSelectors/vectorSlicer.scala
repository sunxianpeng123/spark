package sparkMllib.ml_df.FeatureSelectors

import java.util

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.attribute.{Attribute, AttributeGroup, NumericAttribute}
import org.apache.spark.ml.feature.VectorSlicer
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SparkSession}

object vectorSlicer {
  /**
    * VectorSlicer是一个转换器，它采用特征向量，并输出一个新的特征向量与原始特征的子阵列。从向量列中提取特征很有用。
    * VectorSlicer接受具有指定索引的向量列，然后输出一个新的向量列，其值通过这些索引进行选择。有两种类型的指数：
    * 代表向量中的索引的整数索引，setIndices()。
    * 表示向量中特征名称的字符串索引，setNames()，此类要求向量列有AttributeGroup，因为实现在Attribute的name字段上匹配。
    * 整数和字符串的规格都可以接受。此外，您可以同时使用整数索引和字符串名称。必须至少选择一个特征。重复的功能是不允许的，所以选择的索引和名称之间不能有重叠。请注意，如果选择了功能的名称，则会遇到空的输入属性时会抛出异常。
    * 输出向量将首先（按照给定的顺序）对所选索引的特征进行排序，其次是所选择的名称（按照给定的顺序）。
    * Examples
    * 假设我们有一个含有userFeatures列的DataFrame：
            userFeatures
          ------------------
           [0.0, 10.0, 0.5]
    *userFeatures是一个包含三个用户功能的向量列。假设userFeature的第一列全部为0，因此我们要删除它并仅选择最后两列。
    * VectorSlicer使用setIndices（1,2）选择最后两个元素，然后生成一个名为features的新向量列：
        userFeatures     | features
        ------------------|-----------------------------
         [0.0, 10.0, 0.5] | [10.0, 0.5]
    *假设我们对userFeatures具有潜在的输入属性，即["f1", "f2", "f3"]，那么我们可以使用setNames("f2", "f3")来选择它们。
            userFeatures     | features
            ------------------|-----------------------------
             [0.0, 10.0, 0.5] | [10.0, 0.5]
             ["f1", "f2", "f3"] | ["f2", "f3"]
    */
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("BinarizerExample")
      .master("local[*]")
      .getOrCreate()
    val data = util.Arrays.asList(
      Row(Vectors.sparse(3, Seq((0, -2.0), (1, 2.3)))),
      Row(Vectors.dense(-2.0, 2.3, 0.0))
    )

    val defaultAttr = NumericAttribute.defaultAttr
    val attrs = Array("f1", "f2", "f3").map(defaultAttr.withName)
    val attrGroup = new AttributeGroup("userFeatures", attrs.asInstanceOf[Array[Attribute]])

    val dataset = spark.createDataFrame(data, StructType(Array(attrGroup.toStructField())))

    val slicer = new VectorSlicer().setInputCol("userFeatures").setOutputCol("features")

    slicer.setIndices(Array(1)).setNames(Array("f3"))
    // or slicer.setIndices(Array(1, 2)), or slicer.setNames(Array("f2", "f3"))

    val output = slicer.transform(dataset)
    output.show(false)
  }
}
