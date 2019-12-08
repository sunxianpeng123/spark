package sparkSQL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/17
  * \* Time: 17:10
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object CreateDataset {
  case class Person(name: String, age: Long)
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().appName("test").config("spark.some.config.option","some value").master("local[*]").getOrCreate()
    import spark.implicits._

    val caseClassDS = Seq(Person("Andy", 32)).toDS()
    caseClassDS.show()
    // Encoders for most common types are automatically provided by importing spark.implicits._
    val primitiveDS = Seq(1, 2, 3).toDS()
    primitiveDS.map(_ + 1).collect().foreach(println) // Returns: Array(2, 3, 4)
    // DataFrames转换成Dataset，　Mapping will be done by name－－－－－－－－－－－－－ DataFrames can be converted to a Dataset by providing a class
    val path = "F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json"
    val df = spark.read.json(path)
    val peopleDS = df.as[Person]
    peopleDS.show()



  }
}

