package sparkMllib.ml_df.FeatureTransformers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.{NGram, RegexTokenizer, StopWordsRemover, Tokenizer}
import org.apache.spark.sql.SparkSession

object TokenizerStopWordsNgram {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder
      .appName("TokenizerExample")
        .master("local[*]")
      .getOrCreate()
    import spark.implicits._
    // $example on$
    val sentenceDataFrame = spark.createDataFrame(Seq(
      (0, "Hi I heard about Spark"),
      (1, "I wish Java could use case classes"),
      (2, "Logistic,regression,models,are,neat")
    )).toDF("id", "sentence")

    val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")

    val tokenized = tokenizer.transform(sentenceDataFrame)
    tokenized.select( "id","sentence","words").show(false)


    val regexTokenizer = new RegexTokenizer()
      .setInputCol("sentence")
      .setOutputCol("words")
      .setPattern("\\W") // alternatively .setPattern("\\w+").setGaps(false)

    //val countTokens = udf { (words: Seq[String]) => words.length }



    val regexTokenized = regexTokenizer.transform(sentenceDataFrame)
    regexTokenized.select("sentence", "words").show(false)
    /**
      *对于某些语言的默认停止词是通过调用 StopWordsRemover.loadDefaultStopWords(language) 设置的，
      * 可用的选项为"丹麦"，"荷兰语"、"英语"、"芬兰语"，"法国"，"德国"、"匈牙利"、"意大利"、"挪威"、"葡萄牙"、"俄罗斯"、"西班牙"、"瑞典"和"土耳其"。
      * 布尔型参数 caseSensitive 指示是否区分大小写 （默认为否）。
      */
    val remover = new StopWordsRemover()
      .setInputCol("words")
      .setOutputCol("filtered")

    remover.transform(tokenized).show()
    //StopWordsRemover.loadDefaultStopWords("english").foreach(println)

    /**
      * 一个 n-gram是一个长度为n（整数）的字的序列。NGram可用于将输入特征转换成n-grams。
      * N-Gram 的输入为一系列的字符串（例如：Tokenizer分词器的输出）。参数 n 表示每个 n-gram 中单词（terms）的数量。
      * 输出将由 n-gram 序列组成，其中每个 n-gram 由空格分隔的 n 个连续词的字符串表示。如果输入的字符串序列少于n个单词，NGram 输出为空。
      */
    val ngram = new NGram().setN(2).setInputCol("words").setOutputCol("ngrams")

    val ngramDataFrame = ngram.transform(tokenized)
    ngramDataFrame.show(false)
  }
}
