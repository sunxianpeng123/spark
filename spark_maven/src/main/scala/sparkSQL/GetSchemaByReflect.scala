package sparkSQL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.Encoder

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 18:23
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object GetSchemaByReflect {
  case class Person(name: String, age: Int)
  def main(args: Array[String]): Unit = {
    /**
      * // Spark SQL 的 Scala 接口支持自动转换一个包含 case classes 的 RDD 为 DataFrame。 Case class 定义了表的 Schema。Case class 的参数名使用反射读取并且成为了列名。
      * Case class 也可以是嵌套的或者包含像 SeqS 或者 ArrayS 这样的复杂类型。这个 RDD 能够被隐式转换成一个 DataFrame 然后被注册为一个表。表可以用于后续的 SQL 语句。
      */
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().appName("test").config("spark.some.config.option","some value").master("local[*]").getOrCreate()
    // For implicit conversions from RDDs to DataFrames
    import spark.implicits._

    // Create an RDD of Person objects from a text file, convert it to a Dataframe
    val peopleDF = spark.sparkContext
      .textFile("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.txt")
      .map(_.split(","))
      .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
      .toDF()
    // Register the DataFrame as a temporary view
    peopleDF.createOrReplaceTempView("people")
    peopleDF.show()
    // SQL statements can be run by using the sql methods provided by Spark
    val teenagersDF = spark.sql("SELECT name, age FROM people WHERE age BETWEEN 13 AND 19")//.as[(Int,String)]

    // The columns of a row in the result can be accessed by field index
    teenagersDF.map(teenager => "Name: " + teenager(0)).show()
    // or by field name
    teenagersDF.map(teenager => "Name: " + teenager.getAs[String]("name")).show()
    // No pre-defined encoders for Dataset[Map[K,V]], define explicitly
//    implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]
//    // Primitive types and case classes can be also defined as
//    implicit val stringIntMapEncoder: Encoder[Map[String, Int]] = ExpressionEncoder()
//
//    // row.getValuesMap[T] retrieves multiple columns at once into a Map[String, T]
//    teenagersDF.map(teenager => teenager.getValuesMap[Any](List("name", "age"))).collect()
//    // Array(Map("name" -> "Justin", "age" -> 19))
  }
}

