package tools

import org.apache.spark.sql.SparkSession

/**
  * Created by Dell on 2017/6/15.
  */
object testSparkSession {
  def main(args: Array[String]): Unit = {
    val ss=SparkSession.builder.master("local").appName("myapp").getOrCreate()//创建SparkSession
    //val ss1=SparkSession.builder.master("local").appName("myapp").enableHiveSupport().getOrCreate()//创建hiveContext，可以使用该方法来创建SparkSession，以使得它支持Hive：
    val df=ss.read.option("header","true").csv("data.csv")
    df.show()

  }
}
