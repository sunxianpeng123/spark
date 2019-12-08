package control

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by Dell on 2017/6/26.
  */
class testDFSelect {
  def test(sqlDate: DataFrame): Unit = {
    //sqlDate.show()//
    println("列名：：")
    val columns1=sqlDate.columns//查询列名
    for (s <- columns1){
      println(s)
    }
    println("取前几条：：")
    val preDatas=sqlDate.take(3)
    for (s<- preDatas){
      println(s)
    }
    println("转成jsonRDD：：")
//    val jsonRdd=sqlDate.toJSON//collect函数可以提取出所有rdd里的数据项
//    val jsonRddData=jsonRdd.collect()
//    for (s <- jsonRddData){
//      println(s)
//    }
    println("条件查询：：")
    println("filter::")
    val filterF1=sqlDate.filter("room_id=1025977")
    filterF1.show()
//    val filterF2=sqlDate.filter("room_id!=1025977")
//    filterF2.show()
//    val filterF3=sqlDate.filter("room_id>1025977")
//    filterF3.show()
    println("where::")
    val whereF1=sqlDate.where("room_id=1025977")
    whereF1.show()
//    val whereF2=sqlDate.where("room_id>1025977&&platform_id==43")//多条件查询
//    whereF2.show()



  }
}
