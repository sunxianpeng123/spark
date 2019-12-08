import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Dell on 2017/6/13.
  */
object test {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sqlContext=SparkSession.builder().appName("read_hive_data").getOrCreate()
    val sc = sqlContext.sparkContext
    val wordTupRDD = sc.parallelize(List(1,2,3)).map(x=>(x,1))
    val wordTupSumRDD = wordTupRDD.reduceByKey((x, y) => x + y)
    val wordTupSumSortRDD = wordTupSumRDD.sortBy(_._2, false)
    val danmaku: String = wordTupSumSortRDD.map(x => List(x)).reduce((x, y) => x ::: y).mkString("")

    val l:List[String]=List()
    val m:List[Tuple2[String,String]]=List()
    val t=sc.parallelize(l).map(x=>List(x))
    if(t.count()>0){
    val a =t.reduce((x,y)=>x:::y)
      //.mkString("")
      }
    //val mm=sc.parallelize(m).reduceByKey((x,y)=> x+y).sortBy(_._2, false).map(x => List(x)).reduce((x, y) => x ::: y)
    println(t)
  if (l.size!=0){
    println(1)
  }

  }

}
