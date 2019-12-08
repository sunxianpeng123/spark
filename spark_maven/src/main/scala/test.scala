import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by Dell on 2017/6/13.
  */
object test {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sparkSession=SparkSession.builder().appName("read_hive_data").master("local[*]").getOrCreate()
//    val sc = sqlContext.sparkContext
    val sc = sparkSession.sparkContext

  }

}
