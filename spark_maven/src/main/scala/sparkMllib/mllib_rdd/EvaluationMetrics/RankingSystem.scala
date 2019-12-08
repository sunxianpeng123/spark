package sparkMllib.mllib_rdd.EvaluationMetrics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.evaluation.{RankingMetrics, RegressionMetrics}
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.sql.SparkSession

object RankingSystem {
  /**
    * Ranking system（排名系统）
    * 排名算法（通常被作为推荐系统算法）的作用是基于一些训练数据给用户返回一个相关物品或文章的集合。
    * 相关性的定义会根据不同的应用场景而有所差异。
    * 排名系统评估指标则用于在不同的场景中量化排名或者推荐的效果。
    * 某些指标会把系统推荐的文章集合跟实际相关的文章集合做对比，而另外一些指标会显式地结合数值打分。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().master("local[*]").appName("test tfidf").getOrCreate()
    // 读取打分数据
    val ratings = spark.read.textFile("data/mllib/sample_movielens_data.txt").rdd.map { line =>
      val fields = line.split("::")
      Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble - 2.5)
    }.cache()

    // 将打分映射到0或者1，1表示影片应该被推荐
    val binarizedRatings = ratings.map(r => Rating(r.user, r.product,
      if (r.rating > 0) 1.0 else 0.0)).cache()

    // 评分的统计结果
    val numRatings = ratings.count()
    val numUsers = ratings.map(_.user).distinct().count()
    val numMovies = ratings.map(_.product).distinct().count()
    println(s"Got $numRatings ratings from $numUsers users on $numMovies movies.")

    // 建模
    val numIterations = 10
    val rank = 10
    val lambda = 0.01
    val model = ALS.train(ratings, rank, numIterations, lambda)

    // 定义一个函数将打分映射到0和1区间
    def scaledRating(r: Rating): Rating = {
      val scaledRating = math.max(math.min(r.rating, 1.0), 0.0)
      Rating(r.user, r.product, scaledRating)
    }

    // 获取排序后对于每个用户前十个预测值，并映射到0和1区间
    val userRecommended = model.recommendProductsForUsers(10).map { case (user, recs) =>
      (user, recs.map(scaledRating))
    }

    // 假设任何被用户打分为3分或更高（映射到1）的影片为一个相关的影片
    // 并与前十个最相关的影片做对比
    val userMovies = binarizedRatings.groupBy(_.user)
    val relevantDocuments = userMovies.join(userRecommended).map { case (user, (actual,
    predictions)) =>
      (predictions.map(_.product), actual.filter(_.rating > 0.0).map(_.product).toArray)
    }

    // 实例化一个评估指标模型
    val metrics = new RankingMetrics(relevantDocuments)

    // Precision at K
    Array(1, 3, 5).foreach { k =>
      println(s"Precision at $k = ${metrics.precisionAt(k)}")
    }

    // Mean average precision
    println(s"Mean average precision = ${metrics.meanAveragePrecision}")

    // Normalized discounted cumulative gain
    Array(1, 3, 5).foreach { k =>
      println(s"NDCG at $k = ${metrics.ndcgAt(k)}")
    }

    // 获得每个数据点的 Prediction
    val allPredictions = model.predict(ratings.map(r => (r.user, r.product))).map(r => ((r.user,
      r.product), r.rating))
    val allRatings = ratings.map(r => ((r.user, r.product), r.rating))
    val predictionsAndLabels = allPredictions.join(allRatings).map { case ((user, product),
    (predicted, actual)) =>
      (predicted, actual)
    }

    // 使用回归评估指标获得 RMSE
    val regressionMetrics = new RegressionMetrics(predictionsAndLabels)
    println(s"RMSE = ${regressionMetrics.rootMeanSquaredError}")

    // R-squared
    println(s"R-squared = ${regressionMetrics.r2}")
  }

}
