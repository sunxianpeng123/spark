package sparkMllib.ml_df.MLPipelines

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{Vectors,Vector}
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.{Row, SparkSession}

object EstimatorTransformerParam {
  /**
    * 1、Pipelines的主要概念
    * MLlib 将机器学习算法的API标准化，以便将多种算法更容易地组合成单个 Pipeline （管道）或者工作流。本节介绍Pipelines API 的关键概念,其中 Pipeline（管道）的概念主要是受到 scikit-learn 项目的启发.
    * DataFrame（数据模型）：ML API 将从Spark SQL查出来的 DataFrame 作为 ML 的数据集,数据集支持许多数据类型。例如,一个 DataFrame 可以有不同的列储存 text（文本）、feature（特征向量）、true labels（标注）、predictions（预测结果）等机器学习数据类型.
    * Transformer（转换器）：使用 Transformer 将一个 DataFrame 转换成另一个 DataFrame 的算法，例如，一个 ML Model 是一个 Transformer,它将带有特征的 DataFrame 转换成带有预测结果的 DataFrame.
    * Estimator（模型学习器）：Estimator 是一个适配 DataFrame 来生成 Transformer（转换器）的算法.例如,一个学习算法就是一个 Estimator 训练 DataFrame 并产生一个模型的过程.
    * Pipeline（管道）：Pipeline 将多个 Transformers 和 Estimators 绑在一起形成一个工作流.
    * Parameter（参数）：所有的 Transformers 和 Estimators 都已经使用标准的 API 来指定参数.
    * 2、DataFrame
    * 机器学习可以应用于各种各样的数据类型，比如向量，文本，图形和结构化数据.API 采用 Spark Sql 的 DataFrame 就是为了支持各种各样的数据类型.
    * DataFrame 支持许多基本的结构化的数据，参考Spark SQL datatype reference上的一系列支持的类型。另外除了 Spark SQL guide 列举的类型,DataFrame 还支持使用 ML Vector 类型.
    * DataFrame 可以用标准的 RDD 显式或者非显式创建。请参考下面的例子，或者前往 Spark SQL programming guide 查看例子
    * DataFrame 中的列是有名称的.下面的代码示例会使用名称如 "文本","特征"和"标签".
    * 3、Pipelines 组件
    * Transformers（转换器）
    * 转换器是特征变换和机器学习模型的抽象。转换器必须实现transform方法，这个方法将一个 DataFrame 转换成另一个 DataFrame，通常是附加一个或者多个列。比如：
    * 一个特征变换器是输入一个 DataFrame，读取一个列（比如：text），将其映射成一个新列（比如，特征向量），然后输出一个新的 DataFrame 并包含这个映射的列.
    * 一个机器学习模型是输入一个 DataFrame，读取包含特征向量的列,预测每个特征向量的标签,并输出一个新的 DataFrame ，并附加预测标签作为一列.
    * 4、Estimators（模型学习器）
    * Estimators 模型学习器是拟合和训练数据的机器学习算法或者其他算法的抽象。技术上来说, Estimator（模型学习器）实现 fit() 方法，这个方法输入一个 DataFrame 并产生一个 Model 即一个 Transformer（转换器）。
    * 举个例子，一个机器学习算法是一个 Estimator 模型学习器,比如这个算法是 LogisticRegression（逻辑回归），调用 fit() 方法训练出一个 LogisticRegressionModel,这是一个 Model,因此也是一个 Transformer（转换器）.
    * 5、Pipeline组件的参数
    * Transformer.transform() 和 Estimator.fit() 都是无状态。以后,可以通过替换概念来支持有状态算法.
    * 每一个 Transformer（转换器）和 Estimator （模型学习器）都有一个唯一的ID，这在指定参数上非常有用（将在下面进行讨论）.
    * 6、Pipeline
    * 在机器学习中,通常会执行一系列算法来处理和学习模型，比如，一个简单的文本文档处理流程可能包括这几个步骤：
    * 把每个文档的文本分割成单词.
    * 将这些单词转换成一个数值型特征向量.
    * 使用特征向量和标签学习一个预测模型.
    * MLlib 代表一个流水线,就是一个 Pipeline（管道），Pipeline（管道） 包含了一系列有特定顺序的管道步骤（Transformers（转换器） 和 Estimators（模型学习器））。我们将使用这个简单的工作流作为本节的运行示例.
    * 7、如何工作
    * 一个 pipeline 由多个步骤组成，每一个步骤都是一个 Transformer（转换器）或者 Estimator（模型学习器）。这些步骤按顺序执行，输入的 DataFrame 在通过每个阶段时进行转换。
    * 在 Transformer （转换器）步骤中，DataFrame 会调用 transform() 方法；在 Estimator（模型学习器）步骤中，fit() 方法被调用并产生一个 Transformer（转换器）(会成为 PipelineModel（管道模型）的一部分,或者适配 Pipeline ),
    * 并且 DataFrame 会调用这个 Transformer’s（转换器）的transform()方法.
    * 8、细节
    * DAG（有向无环图）Pipelines：管道的步骤被定义为一个有序的数组。上面的例子是一个线性的管道即 Pipelines每个步骤使用的数据都产生于前一个步骤。只要数据流图能构成有向无环图，非线性 Pipelines 也可以创建出来。图当前是通过输每个步骤的输入输出列名来隐含指定的（通常指定为参数）.如果 Pipeline 构成了一个 DAG,则按照拓扑顺序指定阶段.
    * Runtime checking（运行时检查）：由于管道可以对具有不同类型的 DataFrames 进行操作,因此不能使用编译时类型检查。Pipelines 和PipelineModels 在真正运行 Pipeline 之前进行运行时检查。这个类型检查通过 DataFrame schema（描述了 DataFrame 列的数据类型）来完成的.
    * Unique Pipeline stages（唯一性管道阶段）：一个管道的阶段必须是一个唯一实例，比如，相同的实例 myHashingTF不能插入到 Pipeline 两次，因为管道的阶段的 ID 必须是唯一的。但是，不同的实例 myHashingTF1 和myHashingTF2（都是 HashingTF 类型）可以放进相同的 Pipeline,只要通过不同的 ID 创建不同的实例.
    * 9、参数
    * MLib 的 Estimators（模型学习器）和 Transformers（转换器）使用统一的 API 来指定参数.
    * Param 是具有自包含定义的参数，ParamMap 是一组（参数,值）对.
    * 将参数传递给算法主要有两种方式
    * 给实例设置参数,例如,如果 lr 是一个 LogisticRegression实例,调用 lr.setMaxIter(10) 使 lr.fit() 最多10次迭代。这个 API 类似于 spark.mlib 包中的 API.
    * 传递一个 ParamMap 给 fit() 函数和 transform() 函数。ParamMap 的任意参数将会覆盖前面调用实例通过 setter 方法指定的参数.
    * 参数属于特定的 Estimators（模型学习器）和 Transformers（转换器）的实例。例如，如果我们有两个 LogisticRegression 实例 lr1 和 lr2，然后我们可以使用指定的 maxIter 参数来构建ParamMap：ParamMap(lr1.maxIter -> 10, lr2.maxIter -> 20)。如果 Pipeline 中存在两个 maxIter 参数的算法,这就非常有用.
    * 10、保存和加载管道
    * 通常情况下,将模型或管道保存到磁盘上以供将来使用是值得的。在 Spark 1.6，模型导入导出功能被加入到 Pipeline API。大多数基本的 transformers（转换器）是被支持的,包括一些更基本的ML Models。请根据算法 API 文档判断是否支持保存和加载.
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("RegressionMetricsExample")
      .master("local[*]")
      .getOrCreate()
    // Prepare training data from a list of (label, features) tuples.
    val training = spark.createDataFrame(Seq(
      (1.0, Vectors.dense(0.0, 1.1, 0.1)),
      (0.0, Vectors.dense(2.0, 1.0, -1.0)),
      (0.0, Vectors.dense(2.0, 1.3, 1.0)),
      (1.0, Vectors.dense(0.0, 1.2, -0.5))
    )).toDF("label", "features")

    // Create a LogisticRegression instance. This instance is an Estimator.
    val lr = new LogisticRegression()
    // Print out the parameters, documentation, and any default values.
    println("LogisticRegression parameters:\n" + lr.explainParams() + "\n")

    // We may set parameters using setter methods.
    lr.setMaxIter(10)
      .setRegParam(0.01)

    // Learn a LogisticRegression model. This uses the parameters stored in lr.
    val model1 = lr.fit(training)
    // Since model1 is a Model (i.e., a Transformer produced by an Estimator),
    // we can view the parameters it used during fit().
    // This prints the parameter (name: value) pairs, where names are unique IDs for this
    // LogisticRegression instance.
    println("Model 1 was fit using parameters: " + model1.parent.extractParamMap)

    // We may alternatively specify parameters using a ParamMap,
    // which supports several methods for specifying parameters.
    val paramMap = ParamMap(lr.maxIter -> 20)
      .put(lr.maxIter, 30)  // Specify 1 Param. This overwrites the original maxIter.
      .put(lr.regParam -> 0.1, lr.threshold -> 0.55)  // Specify multiple Params.

    // One can also combine ParamMaps.
    val paramMap2 = ParamMap(lr.probabilityCol -> "myProbability")  // Change output column name.
    val paramMapCombined = paramMap ++ paramMap2

    // Now learn a new model using the paramMapCombined parameters.
    // paramMapCombined overrides all parameters set earlier via lr.set* methods.
    val model2 = lr.fit(training, paramMapCombined)
    println("Model 2 was fit using parameters: " + model2.parent.extractParamMap)

    // Prepare test data.
    val test = spark.createDataFrame(Seq(
      (1.0, Vectors.dense(-1.0, 1.5, 1.3)),
      (0.0, Vectors.dense(3.0, 2.0, -0.1)),
      (1.0, Vectors.dense(0.0, 2.2, -1.5))
    )).toDF("label", "features")

    // Make predictions on test data using the Transformer.transform() method.
    // LogisticRegression.transform will only use the 'features' column.
    // Note that model2.transform() outputs a 'myProbability' column instead of the usual
    // 'probability' column since we renamed the lr.probabilityCol parameter previously.
    model2.transform(test)
      .select("features", "label", "myProbability", "prediction")
      .collect()
      .foreach { case Row(features: Vector, label: Double, prob: Vector, prediction: Double) =>
        println(s"($features, $label) -> prob=$prob, prediction=$prediction")
      }
  }
}
