package sparkSQL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 18:37
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object GetSchemaByCode {
  def main(args: Array[String]): Unit = {
    /**
      * 当 case class 不能够在执行之前被定义（例如，records 记录的结构在一个 string 字符串中被编码了，
      * 或者一个 text 文本 dataset 将被解析并且不同的用户投影的字段是不一样的）。一个 DataFrame 可以
      * 使用下面的三步以编程的方式来创建。从原始的 RDD 创建 RDD 的 RowS（行）。Step 1 被创建后，创建
      * Schema 表示一个 StructType 匹配 RDD 中的 Rows（行）的结构。通过 SparkSession 提供的
      * createDataFrame 方法应用 Schema 到 RDD 的 RowS（行）。
      */
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().appName("test").config("spark.some.config.option","some value").master("local[*]").getOrCreate()
    // Create an RDD
    val peopleRDD = spark.sparkContext.textFile("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\/people.txt")

    // The schema is encoded in a string
    val schemaString = "name age"

    // Generate the schema based on the string of schema
    val fields = schemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, nullable = true))
    val schema = StructType(fields)

    // Convert records of the RDD (people) to Rows
    val rowRDD = peopleRDD
      .map(_.split(","))
      .map(attributes => Row(attributes(0), attributes(1).trim))

    // Apply the schema to the RDD
    val peopleDF = spark.createDataFrame(rowRDD, schema)

    // Creates a temporary view using the DataFrame
    peopleDF.createOrReplaceTempView("people")

    // SQL can be run over a temporary view created using DataFrames
    val results = spark.sql("SELECT name FROM people")

    // The results of SQL queries are DataFrames and support all the normal RDD operations
    // The columns of a row in the result can be accessed by field index or by field name
//    results.map(attributes => "Name: " + attributes(0)).show()
  }
}

