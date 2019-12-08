package sparkSQL

import java.util
import java.util.ArrayList

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{DataTypes, StringType, StructField, StructType}

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/18
  * \* Time: 18:37
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object CreateDataFrame {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sparksession=SparkSession.builder().appName("test").master("local[*]").getOrCreate()
    //    val peopleDF=sparksession.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.txt")

    //DataFrame 可以使用下面的三步以编程的方式来创建。

    val peopleRDD=sparksession.sparkContext.textFile("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.txt")
    //第一种创建临时表方式
    val schemaString="name random age"
    val fields=schemaString.split(" ").map(fieldName=>StructField(fieldName,StringType,nullable = true))
    val schema=StructType(fields)


    val rowRDD= peopleRDD.map(_.split(",")).map(attributes=>Row(attributes(0),attributes(1).trim,attributes(2).trim))
    val peopleDF=sparksession.createDataFrame(rowRDD,schema)
    peopleDF.createOrReplaceTempView("tmp")
    //peopleDF.show()
    //    fields.foreach(println)
    //    schema.foreach(println)
    //    rowRDD.foreach(println)
    val results=sparksession.sql("select * from tmp")
    results.show()

    val t = results
      .groupBy("name","random")
      .agg("age"->"collect_set")//Seq[Seq[String]]
      .toDF("name","random","age_set")
    t.show()
    val t1 = t.rdd.map(row=>{
      var giftFormNameSeq: Seq[String]=Seq()
      giftFormNameSeq = row.getSeq(2)
      println("############"+giftFormNameSeq.mkString(","))
    })
    println(t1.count())


    val t2 = t
      .groupBy("name")
      .agg("age_set"->"collect_set")//
      .toDF("name","age_set_set")

    val t3 = t2.rdd.map(row=>{
      var giftFormNameSeq: Seq[Seq[String]]=Seq()
      giftFormNameSeq = row.getSeq(1)
      var res:Seq[String] =Seq.empty
      giftFormNameSeq.foreach(seq=>{
        res = res ++ seq
      })
      println(res.mkString(",,,,"))


    })
    println(t3.count())

System.exit(0)

    //第二种创建临时表方式
    // 参考SparkTopRedisApp
    val structMessageFields = new ArrayList[StructField]()
    structMessageFields.add(DataTypes.createStructField("name", DataTypes.StringType, true));
    structMessageFields.add(DataTypes.createStructField("age", DataTypes.StringType, true));
    val structMessageType = DataTypes.createStructType(structMessageFields)

    val list :java.util.List[Row]=new util.ArrayList[Row]()
    var gb:Row=null
    peopleRDD.foreach(record=>{
      val name=record.split(",")(0)
      val age=record.split(",")(1)
      gb=Row(name,age)
      list.add(gb)
      println("name="+name +" ,age ="+age)
    })

    sparksession.createDataFrame(list, structMessageType).createOrReplaceTempView("tmp")//创建dataframe
    val results2=sparksession.sql("select * from tmp")










  }
}

