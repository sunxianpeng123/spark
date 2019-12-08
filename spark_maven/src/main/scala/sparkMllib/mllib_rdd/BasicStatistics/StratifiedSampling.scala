package sparkMllib.mllib_rdd.BasicStatistics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object StratifiedSampling {
  /**
    * Stratified sampling/分层采样
    * 与spark.mllib中的其他统计功能不同，
    * sampleByKey和sampleByKeyExact可以对键值对的RDD执行分层采样方法。
    * 对于分层采样，键可以被认为是一个标签，该值作为一个特定属性。
    * 例如，key 可以是男人或女人或文档ID，并且相应的 value 可以是人的年龄列表或文档中的单词列表。
    * 1、sampleByKey方法将类似掷硬币方式来决定观察是否被采样，因此需要一次遍历数据，并提供期望的样本大小。
    * 2、sampleByKeyExact需要比sampleByKey中使用的每层简单随机抽样花费更多的资源，但将提供99.99％置信度的确切抽样大小。 p
    * ython当前不支持sampleByKeyExact。
    * sampleByKeyExact() 允许用户精确地采样⌈fk⋅nk⌉∀k∈K⌈fk⋅nk⌉∀k∈K项，其中fk fk是关键kk的期望分数，nk nk是关键kk的键值对的数量， 而KK是一组键。
    * 无需更换的采样需要额外通过RDD以保证采样大小，而替换的采样需要两次额外的通过。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    val data = spark.sparkContext.parallelize(
      Seq((1, 'a'), (1, 'b'), (2, 'c'), (2, 'd'), (2, 'e'), (3, 'f')))

    // 指明每个Key的权重
    val fractions = Map(1 -> 0.1, 2 -> 0.6, 3 -> 0.3)

    // 从每层获取一个近似的样本
//    这里，设置采取10%的1,60%的2和30%的3，因为数据中1,2,3各有2,3,1个样本，所以理想中的抽样结果应该是有0.2个1，1.8个2，0.3个3。接下来用sampleByKey进行抽样：
    val approxSample = data.sampleByKey(withReplacement = false, fractions = fractions)
    println(approxSample.foreach(println))
    // 从每层获取一个确切的样本
    println("======")
//    sampleByKey 和 sampleByKeyExact 的区别在于 sampleByKey 每次都通过给定的概率以一种类似于掷硬币的方式来决定这个观察值是否被放入样本，因此一遍就可以过滤完所有数据，
// 最后得到一个近似大小的样本，但往往不够准确。而 sampleByKeyExtra 会对全量数据做采样计算。对于每个类别，其都会产生 （fk⋅nk）个样本，其中fk是键为k的样本类别采样的比例；
// nk是键k所拥有的样本数。 sampleByKeyExtra 采样的结果会更准确，有99.99%的置信度，但耗费的计算资源也更多。
    val exactSample = data.sampleByKeyExact(withReplacement = false, fractions = fractions)
    println(exactSample.foreach(println))
//    withReplacement 	每次抽样是否有放回
//      fractions 	控制不同key的抽样率
//      seed 	随机数种子



  }
}
