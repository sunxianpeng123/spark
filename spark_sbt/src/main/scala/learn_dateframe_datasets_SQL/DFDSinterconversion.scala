package learn_dateframe_datasets_SQL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{DateType, StructField, StructType}

/**
  * Created by Dell on 2017/7/28.
  */
object DFDSinterconversion {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sparksession=SparkSession.builder().appName("test").master("local[*]").getOrCreate()
//    val peopleDF=sparksession.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.txt")


    //DataFrame 可以使用下面的三步以编程的方式来创建。

    val peopleRDD=sparksession.sparkContext.textFile("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.txt")
    val schemaString="name age"
    val fields=schemaString.split(" ").map(fieldName=>StructField(fieldName,DateType,nullable = true))
    val schema=StructType(fields)
    val rowRDD=peopleRDD.map(_.split(",")).map(attributes=>Row(attributes(0),attributes(1).trim))
    val peopleDF=sparksession.createDataFrame(rowRDD,schema)
    peopleDF.createOrReplaceTempView("tmp")
    //peopleDF.show()
    fields.foreach(println)
    schema.foreach(println)
    rowRDD.foreach(println)
    val results=sparksession.sql("select * from tmp")
    results.rdd.foreach(println);





  }
}
