package sparkSQL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * Created by Dell on 2017/7/10.
  */
object DataSourceJsonDataset {
  /**
    * Spark SQL 可以自动的推断出 JSON 数据集的 schema 并且将它作为 DataFrame 进行加载。
    * 这个转换可以通过使用 SparkSession.read.json() 在字符串类型的 RDD 中或者 JSON 文件。
    * 注意作为 json file 提供的文件不是一个典型的 JSON 文件。每一行必须包含一个分开的独立的有效 JSON 对象。
    * 因此，常规的多行 JSON 文件通常会失败。
    * @param args
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().appName("test").config("spark.some.config.option","some value").master("local[*]").getOrCreate()
    // The path can be either a single text file or a directory storing text files
    val path = "F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json"
    val peopleDF = spark.read.json(path)
    // The inferred schema can be visualized using the printSchema() method
    peopleDF.printSchema()
    // Creates a temporary view using the DataFrame
    peopleDF.createOrReplaceTempView("people")

    // SQL statements can be run by using the sql methods provided by spark
    val teenagerNamesDF = spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19")
    teenagerNamesDF.show()
    // Alternatively, a DataFrame can be created for a JSON dataset represented by
    // an RDD[String] storing one JSON object per string
    val otherPeopleRDD = spark.sparkContext.makeRDD(
      """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil)
    val otherPeople = spark.read.json(otherPeopleRDD)
    otherPeople.show()


  }
}
