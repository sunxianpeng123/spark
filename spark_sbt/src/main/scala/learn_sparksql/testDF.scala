package learn_sparksql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by Dell on 2017/7/10.
  */
object testDF {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    //2.1 契入点：SparkSession
    val sparkSession=SparkSession.builder().appName("test").config("spark.some.config.option","some value").getOrCreate()
    val df=sparkSession.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")
    df.show()
    println("打印出来表结构::")
    df.printSchema()

    println("打印列名为name的所有列内容::")
    df.select("name").show()

    println("选择name和age列，并且age都加1::")
    df.selectExpr("name","age + 1").show()

    println("大于21的记录::")
    df.filter("age > 21")show()

    println("根据年龄分组，并统计个数::")
    df.groupBy("age").count().show()

    //2.4 程序化执行SQL查询
    df.createOrReplaceTempView("people")
    println("临时视图::")
    val sqlDF=sparkSession.sql(" select * from  people")
        sqlDF.show()
    //2.5 全局临时视图
    //临时视图是基于session级别的，创建视图的session一旦挂掉临时视图的生命也就到此为止了，使用全局视图，可以避免这样的惨剧发生。
    println("全局视图::")
    df.createGlobalTempView("people")
    sparkSession.sql("select name from global_temp.people").show()
    //2.8 聚集函数

  }
}
