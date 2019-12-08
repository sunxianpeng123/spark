package learn_sparksql
import org.apache.log4j.{Level, Logger}

import org.apache.spark.sql.{SaveMode, SparkSession}
/**
  * Created by Dell on 2017/7/10.
  */
object testSparkDataSource {
  def main(args: Array[String]): Unit = {
    //3.1 通用Load/Save函数
    //Spark默认数据源格式将被用于所有的操作，默认是parquet文件格式，使用spark.sql.sources.default指定默认文件格式
    Logger.getLogger("org").setLevel(Level.ERROR)

    //2.1 契入点：SparkSession
    val sparkSession=SparkSession.builder().appName("test").config("spark.some.config.option","some value").getOrCreate()
    val peopleDF=sparkSession.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")
    //peopleDF.select("name","age").write.format("parquet").save("namesAndAges.parquet")
    peopleDF.show()
    //在文件上直接执行SQL
    val sqlDF=sparkSession.sql("SELECT name FROM parquet.`namesAndAges.parquet`")//里面符号为~上符号
    sqlDF.show()
    //保存模式.
    //如果有文件存在，新内容会覆盖原有内容
    println("(1)Overwrite::")
    peopleDF.select("name","age").write.mode(SaveMode.Overwrite).save("testOverWrite.parquet")
    val testOverWriteSqlDF=sparkSession.sql("SELECT * FROM parquet.`testOverWrite.parquet`")
    testOverWriteSqlDF.show()
    //(2)Append
    //如果文件存在，就在原有的文件中追加新增内容
    println("(2)Append::")
    //val peopleDF1=sparkSession.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")
    //peopleDF1.select("name","age").write.mode(SaveMode.Append).save("testAppend.parquet")
    val testAppendDF=sparkSession.sql("SELECT * FROM parquet.`testAppend.parquet`")
    testAppendDF.show()
    //（3）Ignore
    // 如果有文件存在， 则不发生任何事情，和create table if not exists 一样的功能
    //peopleDF.select("name", "age").write.mode(SaveMode.Ignore).save("/tmp/test1.parquet")
    //（4）ErrorIfExists
    // 如果文件存在，就报错，默认就是这个模式
    //如果有文件存在， 则不发生任何事情，和create table if not exists 一样的功能
    //peopleDF.select("name", "age").write.mode(SaveMode.ErrorIfExists).save("/tmp/test1.parquet")

    //3.2 Parquets文件格式
  //3.2.1 读取Parquet文件（Loading Data Programmatically）

    val parquetFileDF=sparkSession.read.parquet("namesAndAges.parquet")
    parquetFileDF.createOrReplaceTempView("parquetFile")
    val namesDF=sparkSession.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19")
    //namesDF.map(attributes => "Name: " + attributes(0)).show()
    namesDF.show()



  }
}
