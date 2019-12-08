package sparkSQL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 16:55
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object SparkSqlBase {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().appName("test").config("spark.some.config.option","some value").master("local[*]").getOrCreate()
    // For implicit conversions like converting RDDs to DataFrames
    import spark.implicits._
    println("json datasource==========")
    val df = spark.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")
    df.show()
    // Print the schema in a tree format
    println(" Print the schema in a tree format")
    df.printSchema()
    println("Select only the \"name\" column")
    df.select("name").show()
    println("Select everybody, but increment the age by 1")
    df.select($"name", $"age" + 1).show()
    println("Select people older than 21")
    df.filter($"age" > 21).show()
    println("Count people by age")
    df.groupBy("age").count().show()
    /**
      *
      */
    println("SparkSession 使应用程序的 SQL 函数能够以编程的方式运行 SQL 查询并且将查询结果以一个 DataFrame")
    df.createOrReplaceTempView("people")

    val sqlDF = spark.sql("SELECT * FROM people")
    sqlDF.show()

  }
}

