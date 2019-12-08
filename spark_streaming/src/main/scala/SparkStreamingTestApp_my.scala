
import bean.DateBean
import com.google.gson.{Gson, JsonParser}
import control.handler
import dao.dbConnect
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import redis.clients.jedis.{JedisPool, JedisPoolConfig, Pipeline}
import tools.{Helper, PropertyUtil}


object SparkStreamingTestApp_my {
  handler.propertyUtilInit()
  var gs =new Gson();
  var jsonParse = new JsonParser();
  var sc: SparkContext = null
  var startDateDay: String = null
  var endDateDay: String = null
  var dateHour: String = null
  var tableName:String=null
  val  chatTypeName="chat"

  //  val properties= new Properties()
  //  val filePath = "db_streaming_test.properties"
  //  properties.load(new FileInputStream(filePath))
  var platSet=PropertyUtil.getString("platIDS").split(",").toSet//properties.getProperty("platIDS").split(",").toSet
  val redisIP =PropertyUtil.getString("redisIp")//properties.getProperty("redisIp")
  val redis_mesg_anchor_pre= PropertyUtil.getString("redis_mesg_anchor_pre")//properties.getProperty("redis_mesg_anchor_pre")
  val redisPort= PropertyUtil.getInt("redisPort",6379)//properties.getProperty("redisPort").toInt
  val redisDB=PropertyUtil.getInt("redisDB",0)// properties.getProperty("redisDB").toInt
  var JedisPool:JedisPool=null
  //var platSet=Set[String]("1")
  def main(args: Array[String]) {
    /**win本地hosts文件需要配置kafka集群信息
      * 113.xxx.xxx.xxx Kafka-game-01
      * 219.xxx.xxx.xxx Kafka-01
      */
    val conf = new SparkConf().setAppName("spark_streaming_test")//.setMaster("local[*]")
    sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")
    val ssc = new StreamingContext(sc, Seconds(60))
    platSet= platSet.map(x=>("node-bullet-crawler-"+x))
    println(platSet)
    val brokers = "Kafka-01:9092"//broker 集群
    //    val brokers = "Kafka-game-01:9092"
    val kafkaParams = Map[String, String](
      "fetch.message.max.bytes" -> "20971520",//控制在一个请求中获取的消息的字节数。 这个参数在0.8.x中由fetch.message.max.bytes,fetch.min.bytes取代
      "metadata.broker.list" -> brokers, //使用这个参数传入boker和分区的静态信息，如host1:port1,host2:port2, 这个可以是全部boker的一部分
      "serializer.class" -> "kafka.serializer.StringEncoder" ,//必须实现kafka.serializer.Encoder接口，将T类型的对象encode成kafka message
      "group.id" -> "test_stream_1127_123" )////用于标识这个消费者属于哪个消费团体
    //    "auto.offset.reset" -> "latest",//如果没有初始化偏移量或者当前的偏移量不存在任何服务器上，可以使用这个配置属性可以使用这个配置，latest自动重置偏移量为最新的偏移量
    //"enable.auto.commit" -> (false: java.lang.Boolean)如果是true，则这个消费者的偏移量会在后台自动提交

    //    // Create a direct stream
    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, platSet)//platSet为平台列表
    println(kafkaStream)
    val events = kafkaStream.map(line => {

      var msg:scala.Array[((String,String),(Int,Double,String,Int,Int,Int))]=null
      try {

        println(line._2)
        val je=jsonParse.parse(line._2);
        var dbArr:Array[DateBean]=null
        if(je.isJsonArray){
          dbArr= gs.fromJson(je,classOf[Array[DateBean]]);
        }
        else{
          val dbb= gs.fromJson(je,classOf[DateBean]);
          dbArr= new Array[DateBean](1)
          dbArr(0)=dbb
        }
        msg=dbArr.map(m=>{
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
          ((platID,roomID),(msgCount ,giftPrice,msg,msgIdSet.size,giftIdSet.size,giftNum))
        }
        )}
      catch {
        case e: Exception =>println(e.getMessage)
      }
      msg

    }).filter(t=>(t!=null && t.length!=0)).flatMap(t=>t).filter(x=>(x._2._2!=0d || x._2._1!=0 || x._2._4!=0 || x._2._5!=0 || x._2._6!=0 ))
    //    events.print()
    try {
      events.foreachRDD(x=>{
        if(x!=null) {
          println( x.count()+"---------handlerDate start  " + Helper.getNowDate() + "--------------")
          val date = x.reduceByKey((e1, e2) => (e1._1 + e2._1, e1._2 + e2._2, e1._3+e2._3,e1._4+e2._4,e1._5+e2._5,e1._6+e2._6))
          val dateCount=date.count()
          if (dateCount>0) {
            val doData = date.collect()
            val jpp = getConnect()
            val jd = jpp.getResource()
            jd.select(redisDB)
            val db = new dbConnect()
            db.init()
            //var tempMap:java.util.Map [java.lang.String,java.lang.Double] =null
            //var hashMapk:mutable.Map[String,java.util.Map [java.lang.String,java.lang.Double] ] = mutable.Map()
            val dotime = System.currentTimeMillis() / 1000.toLong
            val pipeline: Pipeline = jd.pipelined();
            doData.foreach(data => {
              //tempMap  =  new  util.HashMap [java.lang.String,java.lang.Double]()
              //var tempMap:java.util.Map [java.lang.String,java.lang.Double] =  null
              val _p = data._1._1.toInt
              val _r = data._1._2
              //              pipeline.zadd(redis_mesg_anchor_pre + "_" + _p + "_" + _r, dotime, data._2._3)
            })
            pipeline.sync();
            jd.close()
            println("addDate"+doData.length)
            //            db.insertRealTime_statistics(doData, Helper.getNowDateFormat2())
            //            db.closeConnect()
          }
          println("---------handlerDate end" + Helper.getNowDate() + "--------------")
        }
      })

    }
    catch {
      case e: Exception =>println(e.getMessage)
    }
    ssc.start()
    ssc.awaitTermination()
  }



  //
  //
  //  def wordCountProcess(content: String ):String ={
  //      val words = WordSegmenter.seg(content, SegmentationAlgorithm.BidirectionalMaximumMatching).toArray
  //    val regex="""^\d+$"""
  //    val fileDate = words.filter(!_.toString.matches(regex))
  //    //      val fileDate = words.map(line => {
  ////        var reWord=line.toString
  ////        if (line.toString.matches("6{1,100}")) {
  ////          reWord = "666"
  ////        }
  ////        if (line.toString.matches("23{1,100}")) {
  ////
  ////          reWord = "233"
  ////        }
  ////        if (line.toString.matches("哈{1,100}")) {
  ////          reWord = "哈哈哈"
  ////        }
  ////        reWord
  ////      })
  //      val serializeWords = for (e <- fileDate) yield e.toString
  //
  //      val result = serializeWords.toList.mkString(" ")
  //    result
  //
  //  }


  def  getConnect() :JedisPool ={
    if(JedisPool==null){
      val config =new JedisPoolConfig
      config.setTestOnBorrow(true)
      config.setTestOnReturn(true)
      JedisPool = new JedisPool(config, redisIP, redisPort)
      println(redisIP+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+redisPort)
    }
    JedisPool
  }

}