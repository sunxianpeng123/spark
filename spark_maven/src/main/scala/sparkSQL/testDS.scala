package sparkSQL

import org.apache.spark.sql.SparkSession

/**
  * Created by Dell on 2017/7/10.
  */
object testDS {
  def main(args: Array[String]): Unit = {
    case class Person(name: String, age: Long)
    val sparkSession=SparkSession.builder().appName("test").config("spark.some.config.option","some value").master("local[*]").getOrCreate()
    val peopleDS=sparkSession.read.parquet("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")
    val ageCol=peopleDS("age")
    println(ageCol)
  }
}
