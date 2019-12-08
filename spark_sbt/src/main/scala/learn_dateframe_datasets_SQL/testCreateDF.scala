package learn_dateframe_datasets_SQL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by Dell on 2017/7/27.
  */
object testCreateDF {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sparksession=SparkSession.builder().appName("test").master("local[*]").getOrCreate()
    val df=sparksession.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")
    df.printSchema()
    df.createOrReplaceTempView("tmp")
    val sqlDF=sparksession.sql("select * from tmp")
    sqlDF.show()









  }
}
