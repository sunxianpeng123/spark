package sparkSQL

import java.io.FileInputStream
import java.util.Properties

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/18
  * \* Time: 15:06
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object DataSourceJDBC {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder()
      .appName("Spark Hive Example")
      .master("local[*]")
      .getOrCreate()
    println("读mysql===============================================================")
    println("method 1======================")
    val jdbcDF_1 = spark.read
      .format("jdbc")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("url","jdbc:mysql://192.168.120.158:3306")
      .option("dbtable", "plat_info.anchor_tag_info")
      .option("user", "root")
      .option("password", "1qaz@WSX3edc")
      .load()
//    jdbcDF11.show()
    jdbcDF_1.take(1).foreach(println)


    println("method 2======================")
    val jdbcDF_2 = spark.read.format("jdbc").options(
      Map(
        "driver" -> "com.mysql.jdbc.Driver",
        "url" -> "jdbc:mysql://192.168.120.158:3306",
        "dbtable" -> "plat_info.anchor_tag_info",
        "user" -> "root",
        "password" ->  "1qaz@WSX3edc"
        )).load()
    jdbcDF_2.take(1).foreach(println)


    println("method 3======================")
    val readConnProperties1 = new Properties()
    readConnProperties1.put("driver", "com.mysql.jdbc.Driver")
    readConnProperties1.put("user", "root")
    readConnProperties1.put("password","1qaz@WSX3edc")
    readConnProperties1.put("fetchsize", "3")
    val jdbcDF_3 = spark.read.jdbc(
      "jdbc:mysql://192.168.120.158:3306",
      "plat_info.anchor_tag_info",
      readConnProperties1)
    jdbcDF_3.take(1).foreach(println)


    println("method 4======================")
    val readConnProperties4 = new Properties()
    readConnProperties4.put("driver", "com.mysql.jdbc.Driver")
    readConnProperties4.put("user", "root")
    readConnProperties4.put("password","1qaz@WSX3edc")
    readConnProperties4.put("fetchsize", "3")
    val jdbcDF_4 = spark.read.jdbc(
      "jdbc:mysql://192.168.120.158:3306",
      "(select * from plat_info.anchor_tag_info limit 1) test",  // 注意括号和表别名，必须得有，这里可以过滤数据
      readConnProperties4)
    jdbcDF_4.show()


    println("method 5======================")
    val readConnProperties2 = new Properties()
    readConnProperties2.put("driver", "com.mysql.jdbc.Driver")
    readConnProperties2.put("user", "root")
    readConnProperties2.put("password","1qaz@WSX3edc")
    readConnProperties2.put("fetchsize", "3")
    val columnName = "platform_id"
    val lowerBound = 1
    val upperBound = 6
    val numPartitions = 3
    val jdbcDF_5 = spark.read.jdbc(
      "jdbc:mysql://192.168.120.158:3306",
      "plat_info.anchor_tag_info",
      columnName,
      lowerBound,
      upperBound,
      numPartitions,
      readConnProperties2)
    jdbcDF_5.take(1).foreach(println)


    println("method 6======================")
    val readConnProperties3 = new Properties()
    readConnProperties3.put("driver", "com.mysql.jdbc.Driver")
    readConnProperties3.put("user", "root")
    readConnProperties3.put("password","1qaz@WSX3edc")
    readConnProperties3.put("fetchsize", "3")
    val arr = Array(
      (1, 50),
      (2, 60))
    // 此处的条件，既可以分割数据用作并行度，也可以过滤数据
    val predicates = arr.map {
      case (platform_id, room_id) =>
        s" gender = $platform_id " + s" AND age < $room_id "
    }
    val predicates1 =
      Array(
        "2017-05-01" -> "2017-05-20",
        "2017-06-01" -> "2017-06-05").map {
        case (start, end) =>
          s"cast(create_time as date) >= date '$start' " + s"AND cast(create_time as date) <= date '$end'"
      }
    jdbcDF_1.printSchema()
    jdbcDF_2.printSchema()
    println(jdbcDF_1.count())
    println(jdbcDF_2.count())
    val unionDf=jdbcDF_1.union(jdbcDF_2)
    println(unionDf.count())


//    val jdbcDF_6 = spark.read.jdbc(
//      "jdbc:mysql:///192.168.120.158:3306",
//      "plat_info.anchor_tag_info",
//      predicates,
//      readConnProperties3)
//    jdbcDF_6.show()
    println("写mysql=================================成功==============================")
    import spark.implicits._
    val dataList: List[(Int, String, Int, Double, Int)] = List(
      (1, "12345",1,1.0 ,2),
      (2, "678910", 1,1.0,2 ))

//    val colArray: Array[String] = Array("platfrom_id","room_id","tag_id","score","iterm")
//    val df = dataList.toDF(colArray: _*)
//    df.write.mode("append").format("jdbc").options(
//      Map(
//        "driver" -> "com.mysql.jdbc.Driver",
//        "url" -> "jdbc:mysql://192.168.120.158:3306",
//        "dbtable" -> "plat_info.anchor_tag_info",
//        "user" -> "root",
//        "password" -> "1qaz@WSX3edc",
//        "batchsize" -> "1000",
//        "truncate" -> "true")).save()



    val readConnProperties = new Properties()
    readConnProperties.put("driver", "com.mysql.jdbc.Driver")
    readConnProperties.put("user", "root")
    readConnProperties.put("password","1qaz@WSX3edc")
    readConnProperties.put("fetchsize", "3")
    val jdbcDF_411 = spark.read.jdbc(
      "jdbc:mysql://192.168.120.158:3306",
      "(select platform_id,room_id as from_id,tag_id,score,iterm,statistics_start_date,statistics_end_date,update_time from plat_info.anchor_tag_info limit 1) test",  // 注意括号和表别名，必须得有，这里可以过滤数据
      readConnProperties)
    jdbcDF_411.show()
    jdbcDF_411.write.mode("append").jdbc("jdbc:mysql://192.168.120.158:3306/plat_info","audience_tag_info",readConnProperties)
    spark.stop()
  }
}

