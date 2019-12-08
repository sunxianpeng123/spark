package sparkRDD

import java.util.{ArrayList, Properties}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.types.{DataTypes, StructField}
import org.apache.spark.sql.{Row, SparkSession}
/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/11
  * \* Time: 14:55
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object testRDDDFUnion {
  def main(args: Array[String]): Unit = {
    val structMessageFields = new ArrayList[StructField]()
    structMessageFields.add(DataTypes.createStructField("platform_id", DataTypes.IntegerType, true));
    structMessageFields.add(DataTypes.createStructField("room_id", DataTypes.StringType, true));
    structMessageFields.add(DataTypes.createStructField("tag_id", DataTypes.StringType, true));
    structMessageFields.add(DataTypes.createStructField("score", DataTypes.StringType, true));
    structMessageFields.add(DataTypes.createStructField("iterm", DataTypes.StringType, true));
    val structMessageType = DataTypes.createStructType(structMessageFields)

    Logger.getLogger("org").setLevel(Level.ERROR)
    val sqlContext =SparkSession.builder().master("local[*]").appName("test").getOrCreate()
    var rdd1 = sqlContext.sparkContext.makeRDD(List(Row(1,2),Row(1,2),Row(1,2),Row(1,2)))
    var rdd2 = sqlContext.sparkContext.makeRDD(List(Row(1,2),Row(1,2),Row(1,2),Row(1,2)))
    val u=rdd1.union(rdd2).collect
    rdd1.foreach(println)
    println("******")
    rdd2.foreach(println)
    println("********")
    u.foreach(println)
    println("*****************")
    import sqlContext.implicits._

    val readConnProperties4 = new Properties()
    readConnProperties4.put("driver", "com.mysql.jdbc.Driver")
    readConnProperties4.put("user", "root")
    readConnProperties4.put("password","1qaz@WSX3edc")
    readConnProperties4.put("fetchsize", "3")
    val df1 = sqlContext.read.jdbc(
      "jdbc:mysql://192.168.120.158:3306",
      "(select * from plat_info.anchor_tag_info limit 1,2) test",  // 注意括号和表别名，必须得有，这里可以过滤数据
      readConnProperties4)

    val df2 = sqlContext.read.jdbc(
      "jdbc:mysql://192.168.120.158:3306",
      "(select * from plat_info.anchor_tag_info limit 5,2) test",  // 注意括号和表别名，必须得有，这里可以过滤数据
      readConnProperties4)
    val dfU=df1.union(df2)
    df1.show()
    df2.show()
    dfU.show()

  }
}

