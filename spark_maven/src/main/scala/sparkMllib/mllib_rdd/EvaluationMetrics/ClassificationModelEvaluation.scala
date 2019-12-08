package sparkMllib.mllib_rdd.EvaluationMetrics

object ClassificationModelEvaluation {
  /**
    * 虽然分类算法的种类有很多，但是分类模型的评估方法的原理都比较相似。在一个有监督分类问题中，对于每一个数据点都会存在一个真实的分类值（标签）和一个模型生成的预测分类值。基于这两个值，每个数据点的结果都可以被归纳为以下四个类别中的一种：
    * True Positive（TP）- 真实标签为正并且预测正确
    * True Negative（TN）- 真实标签为负并且预测正确
    * False Positive（FP）- 真实标签为负但是预测为正
    * False Negative（FN）- 真实标签为正但是预测为负
    * 上面四种值是大多数分类器评估指标的基础。如果仅仅依靠最基本的准确率（预测是对的还是错的）去评估一个分类器的效果的话，这往往不是一个好的指标，原因在于一个数据集可能各个类别的分布非常不平衡。
    * 举例来说，如果一个预测欺诈的模型是根据这样的数据集设计出来的：95%的数据点是非欺诈数据，5%的数据点是欺诈数据，那么一个简单的分类器去预测非欺诈时，不考虑输入的影响，它的准确率将会是95%。
    * 因此，我们经常会使用 precision 和 recall 这样的指标，因为他们会将“错误”的类型考虑进去。在大多数应用中，precision 和 recall 会被合并为一个指标来在他们之间进行一些平衡，我们称这个指标为 F-measure。
    * @param args
    */
  def main(args: Array[String]): Unit = {

  }
}
