package control


import dao.dbConnect
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by Dell on 2017/6/14.
  */
class myHandler {
  def handleTop(sqlDate: DataFrame,sqlContext:SparkSession) {
    var wordArr:Array[String]=null
    var wordWeight:Array[String]=null
    //sqlDate.show()

    val job1=sqlDate.rdd.map(line=>{

      val wordArr=line.getString(3).split("\\(")
      var tList:List[Tuple2[String,Int]]=List()
      var arg:Array[String]=null;
      var wordCount:String="";
      var word:String=""

      for(s<- wordArr){
        arg=s.split(",")
        word=arg(0)

        if(!word.trim.equals("")){
          wordCount=arg(1).split("\\)")(0)
          //println(word,wordCount)
          tList=Tuple2(word,wordCount.toInt)::tList
        }

      }
      tList
    }).reduce((x,y)=>x:::y)//list----map返回rdd，reduce返回值

    val sc=sqlContext.sparkContext//SparkSession.sparkContext方法将会返回内置的SparkContext
    val job1rdd=sc.parallelize(job1)

    val job2=job1rdd.reduceByKey((x,y)=>x+y)
    val job3=job2.sortBy(_._2,false)

    job3.foreach(println)
    println(job1rdd.count())
    println(job2.count())

    //sc.stop()

  }
}

