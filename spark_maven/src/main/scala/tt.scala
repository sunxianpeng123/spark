import breeze.linalg.DenseVector
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/23
  * \* Time: 18:50
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object tt {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sqlContext = SparkSession
      .builder()
      .appName("heat")
      .master("local[*]")
      .getOrCreate()
    val rddt=sqlContext.sparkContext.parallelize(List((null,"a"),(null,"b"),(null,"c"),(null,"d")))
    val data = sqlContext.sparkContext.parallelize(List(1,2,3,4,5,6,7,8,9,10))
    val acc = sqlContext.sparkContext.accumulator(0)
    val value = data.map(x => {
      acc += x
      x
    })
    println(acc)
    println(value.count())


    sqlContext.stop()














  }
}

