package control

import dao.dbConnect
import org.apache.spark.sql.DataFrame

/**
  * Created by xiangjia on 2016/12/29 0029.
  */
class handler {

  def handleTop(sqlDate: DataFrame) {

    dbConnect.delTopRankList
    val resultList= dbConnect.getRankSchedule//从rank_config表中取出isRunAble =1的id,key_word,weight_proportion放在list中
    var wordArr:Array[String]=null
    var wordWeight:Array[String]=null
  //  var job1:RDD[(Int,String,String,Int,Int)]=null
 //   for (r:rankScheduleBean<- resultList){
    for ( j<-0 until resultList.size()){
      println("compare word "+resultList.get(j).key_word)
      wordArr= resultList.get(j).key_word.split(",")
      wordWeight=resultList.get(j).weight_proportion.split(",")
      val job1 =sqlDate.rdd.map( line => {//数据转换？
         val wordArg= line.getString(3).split("\\(")//取出房间id对应的（词，词频）
         var count:Int =0
         var compareCount:Int =0
         var word:String=""
         var wordCount:String="";
         var arg:Array[String]=null;

         for(s <- wordArg ;if count < 100 ){//
           arg=s.split(",")
           word=arg(0)
           if(!word.trim.equals("")){
             for( i<- 0 until  wordArr.length){
                  if(word.contains(wordArr(i)))
                   {
                     wordCount=arg(1).split("\\)")(0)
                     compareCount=compareCount+(wordWeight(i).toInt*wordCount.toInt)
                   }
             }
             count=count+1
           }
         }
         (line.getInt(0),line.getString(1),line.getString(2),compareCount,resultList.get(j).id)
       }
      ) .filter(_._4>0)
       //job1.foreachPartition(dbConnect.insertRankList)
      //dbConnect.insertRankList(job1.collect().toIterator)//插入数据库
     }
   }




}
