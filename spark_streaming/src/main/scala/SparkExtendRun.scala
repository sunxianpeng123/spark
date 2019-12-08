import bean.DateBean
import com.google.gson.{Gson, JsonParser}
import control.handler
import dao.dbConnect
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}
import redis.clients.jedis.{JedisPool, JedisPoolConfig, Pipeline}
import tools.{Helper, PropertyUtil}


object SparkExtendRun {
  handler.propertyUtilInit()
  var gs =new Gson();
  var jsonParse = new JsonParser();
  var sc: SparkContext = null
  var startDateDay: String = null
  var endDateDay: String = null
  var dateHour: String = null
  var tableName:String=null
  val  chatTypeName="chat"
  var platSet=Set("15")
  var JedisPool:JedisPool=null
  //var platSet=Set[String]("1")
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("spark_streaming_test")//.setMaster("local[*]")
    sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")
    val ssc = new StreamingContext(sc, Seconds(5))
    platSet= platSet.map(x=>("node-bullet-crawler-"+x))
    println(platSet)
    val brokers = PropertyUtil.getString("k")//"Kafka-01:9092"//broker 集群
    println(brokers)
    val kafkaParams = Map[String, String](
      "fetch.message.max.bytes" -> "20971520",
      "metadata.broker.list" -> brokers,
      "serializer.class" -> "kafka.serializer.StringEncoder" ,
      "group.id" -> "test_stream_1127_123" )
    ssc.checkpoint("data/")
    //    // Create a direct stream
    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, platSet)//platSet为平台列表
    println(kafkaStream)
    val events = kafkaStream.map(line => {
      var msg:scala.Array[((String,String),(Int,Double,String,Int,Int,Int))]=null
      val res :scala.Array[(String,Int)]=null
      try {
        //        println(line._2)
        val je=jsonParse.parse(line._2);
        var dbArr:Array[DateBean]=null
        if(je.isJsonArray()){
          dbArr= gs.fromJson(je,classOf[Array[DateBean]]);
        }
        else{
          val dbb= gs.fromJson(je,classOf[DateBean]);
          dbArr= new Array[DateBean](1)
          dbArr(0)=dbb
        }
        val res=dbArr.map(m=>{
          var msgCount=0
          var giftPrice=0d
          var msg=""
          var msgIdSet:Set[String]=Set()
          var giftIdSet:Set[String]=Set()
          var giftNum=0
          var platID=""
          var roomID=""
          if(m!=null & m.item!=null &m.item.length>0){
            platID=m.sid
            roomID=m.roomid
            m.item.foreach(x=>
            {
              if(x!=null){
                if(x.typeName.equals(chatTypeName)){
                  msgCount=msgCount+1;
                  msgIdSet=msgIdSet+x.fromid
                  //msg= msg+x.time+"$$"+x.content.replaceAll("$","")+"$"
                  //修改方便拆分字符串
                  msg= msg+x.time+"$"+x.content.replaceAll("$","")+"$$"
                } else{
                  if(!x.price.trim.equals("")){
                    giftPrice=giftPrice+(x.price.toDouble*x.count)
                    giftIdSet=giftIdSet+x.fromid
                    giftNum=giftNum+x.count
                  }
                }
              }
            })
          }
          //          ((platID,roomID),(msgCount ,giftPrice,msg,msgIdSet.size,giftIdSet.size,giftNum))
          (roomID,1)
        }
        )}
      catch {
        case e: Exception =>println(e.getMessage)
      }
      //      msg
      res
    }).filter(t=>t!=null && t.length!=0)//.filter(t=>(t!=null && t.length!=0)).flatMap(t=>t).filter(x=>(x._2._2!=0d || x._2._1!=0 || x._2._4!=0 || x._2._5!=0 || x._2._6!=0 ))
//    events.foreachRDD(rdd=>{
//      if(rdd!=null){
//        println( "---------handlerDate start  " + Helper.getNowDate() + "--------------")
////        println(rdd)
////        val data =rdd.reduceByKey((e1,e2)=>e1+e2)
//        val data =rdd.flatMap(tup=>{tup}).reduceByKey(_+_)
//        val dataCount=data.count()
//        if (dataCount>0) {
//          val doData = data.collect()
//          doData.foreach(data => {
//            val _k=data._1
//            val _v=data._2
//          })
//          println("addDate"+doData.length)
//        }
//        println("---------handlerDate end" + Helper.getNowDate() + "--------------")
//      }
//    })
    val windows=  events.window(Seconds(15),Seconds(15))
    windows.foreachRDD(rdd=>{
    })

    ssc.start()
    ssc.awaitTermination()
  }

  //定义一个匿名函数去把网页热度上一次的计算结果值和新计算的值相加，得到最新的热度值。

  val addFunc = (currValues: Seq[Int], prevValueState: Option[Int]) => {
    //通过Spark内部的reduceByKey按key规约。然后这里传入某key当前批次的Seq/List,再计算当前批次的总和
    val currentCount = currValues.sum
    // 已累加的值
    val previousCount = prevValueState.getOrElse(0)
    // 返回累加后的结果。是一个Option[Int]类型
    Some(currentCount + previousCount)
  }
}

