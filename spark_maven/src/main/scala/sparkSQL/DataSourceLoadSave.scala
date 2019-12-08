package sparkSQL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * Created by Dell on 2017/7/10.
  */
object DataSourceLoadSave {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().appName("test").config("spark.some.config.option","some value").master("local[*]").getOrCreate()
    println("通用Load/Save函数=================")
    val peopleDF=spark.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")
    peopleDF.select("name", "age").write.format("parquet").save("namesAndAges.parquet")
    //Spark默认数据源格式将被用于所有的操作，默认是parquet文件格式，使用spark.sql.sources.default指定默认文件格式
    println("在文件上直接执行SQL")
    val sqlDF=spark.sql("SELECT name FROM parquet.`F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\users.parquet`")//里面符号为~上符号
    sqlDF.show()
    println(" 保存模式.")
    //Save 操作可以使用 SaveMode，可以指定如何处理已经存在的数据。这是很重要的要意识到这些保存模式
    // 没有利用任何锁并且也不是原子操作。另外，当执行 Overwrite，新数据写入之前会先将旧数据删除。
    //如果有文件存在，新内容会覆盖原有内容
    println("(1)Overwrite::")
    peopleDF.select("name","age").write.mode(SaveMode.Overwrite).save("testOverWrite.parquet")
    val testOverWriteSqlDF=spark.sql("SELECT * FROM parquet.`testOverWrite.parquet`")
    testOverWriteSqlDF.show()
    //(2)Append
    //如果文件存在，就在原有的文件中追加新增内容
    println("(2)Append::")
    //val peopleDF1=sparkSession.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")
    //peopleDF1.select("name","age").write.mode(SaveMode.Append).save("testAppend.parquet")
    val testAppendDF=spark.sql("SELECT * FROM parquet.`testAppend.parquet`")
    testAppendDF.show()
    //（3）Ignore
    // 如果有文件存在， 则不发生任何事情，和create table if not exists 一样的功能
    //peopleDF.select("name", "age").write.mode(SaveMode.Ignore).save("/tmp/test1.parquet")
    //（4）ErrorIfExists
    // 如果文件存在，就报错，默认就是这个模式
    //如果有文件存在， 则不发生任何事情，和create table if not exists 一样的功能
    //peopleDF.select("name", "age").write.mode(SaveMode.ErrorIfExists).save("/tmp/test1.parquet")





  }
}
