package sparkSQL.udf

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/3/29
  * \* Time: 15:01
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object sparkSqlUdf {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sqlContext=SparkSession.builder().appName("test").master("local[*]").getOrCreate()
    //    val peopleDF=sparksession.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.txt")

    //DataFrame 可以使用下面的三步以编程的方式来创建。

    val peopleRDD=sqlContext.sparkContext.textFile("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.txt")
    //第一种创建临时表方式
    val schemaString="name age"
    val fields=schemaString.split(" ").map(fieldName=>StructField(fieldName,StringType,nullable = true))
    val schema=StructType(fields)
    val rowRDD=peopleRDD.map(_.split(",")).map(attributes=>Row(attributes(0),attributes(1).trim))
    val peopleDF=sqlContext.createDataFrame(rowRDD,schema)

    /**
      * 1、UDF对表中的单行进行转换，以便为每行生成单个对应的输出值。例如，大多数 SQL 环境提供 UPPER 函数返回作为输入提供的字符串的大写版本。
      * 用户自定义函数可以在 Spark SQL 中定义和注册为 UDF，并且可以关联别名，这个别名可以在后面的 SQL 查询中使用。
      */
    peopleDF.createOrReplaceTempView("tmp")
    peopleDF.show()
    sqlContext.udf.register("double_age", (age: Double) => age *2)
    println("=====================")
    sqlContext.sql("select name,double_age(age) as age from tmp").show()


    /**
      * 2、用户定义的聚合函数（User-defined aggregate functions, UDAF）同时处理多行，并且返回一个结果，通常结合使用 GROUP BY 语句（例如 COUNT 或 SUM）。
      */
  }
}

