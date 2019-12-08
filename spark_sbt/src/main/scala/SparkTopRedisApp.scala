import java.sql.Date
import java.text.SimpleDateFormat
import java.util
import java.util.ArrayList

import control.{handler, myHandler, testDFSelect,testDFSort}
import dao.dbConnect
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.types.{DataTypes, StructField}
import org.apache.spark.sql.{Row, SparkSession}
import redis.redisConnect

object SparkTopRedisApp {

  val mydayFmt = new SimpleDateFormat("yyyyMMdd")
  var sc: SparkContext = null
  var sqlContext: SparkSession = null//SparkSession实质上是SQLContext和HiveContext的组合（未来可能还会加上StreamingContext），
  // 所以在SQLContext和HiveContext上可用的API在SparkSession上同样是可以使用的。SparkSession内部封装了sparkContext，所以计算实际上是由sparkContext完成的。
  var startDateDay: String = null
  var endDateDay: String = null
  var dateHour: String = null
  var tableName:String=null


  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)
    sqlContext = SparkSession//创建sparksession
      .builder()
      .appName("live_show_app")
      //.config("spark.sql.warehouse.dir", warehouseLocation)
      .getOrCreate()
    startDateDay =  "20170625"//昨日日期
    endDateDay =   "20170625"//昨日日期
    val platform_id=Integer.parseInt("43")//平台号
    val sqlStartDate = new Date(mydayFmt.parse(startDateDay).getTime)
    val sqlEndDate = new Date(mydayFmt.parse(endDateDay).getTime)
    val handler =new handler
    val myHandler=new myHandler
    val testDFSelect=new testDFSelect
    val testDFSort=new testDFSort



    dbConnect.init(sqlStartDate,sqlEndDate,platform_id,"rank_list_1")//初始化dbconnect并连接
    redisConnect.init()//初始化redisConnect并连接

    println("===================AnchorClassify_START========================")
    initMessageTable(startDateDay,platform_id,-1);//作用是什么？？？
    val acSql="select platform_id,room_id,source_link ,word_info   from  tempWord  "//产生的虚拟dataframe中取数据？
    val sqlACDate = sqlContext.sql(acSql).cache()
    //sqlACDate.show()
    handler.handleTop(sqlACDate)
    //myHandler.handleTop(sqlACDate,sqlContext)//全平台弹幕词汇出现数量排名
    //testDFSelect.Test(sqlACDate)//dataframe查询操作
    //testDFSort.Test(sqlACDate)

    sqlACDate.unpersist();//
    println("===================AnchorClassify_END==========================")


    dbConnect.closeConnect()
    redisConnect.close()
    sqlContext.stop()
    System exit 0
  }



  def initMessageTable(date :String,platform_id:Int,group:Int): Unit ={//参数：：开始日期，平台id，-1
    println("====================initMessageTable start================================")
    val structMessageFields = new ArrayList[StructField]()
    structMessageFields.add(DataTypes.createStructField("platform_id", DataTypes.IntegerType, true));
    structMessageFields.add(DataTypes.createStructField("room_id", DataTypes.StringType, true));
    structMessageFields.add(DataTypes.createStructField("source_link", DataTypes.StringType, true));
    structMessageFields.add(DataTypes.createStructField("word_info", DataTypes.StringType, true));//定义list中每个字段
    val structMessageType = DataTypes.createStructType(structMessageFields)

    val list :java.util.List[Row]=new util.ArrayList[Row]()
    val key="word_"+date+"_"+platform_id+"_"
    var fromNum =0
    var toNum=16
    if(group>=0 && group<=15){
      fromNum=group
      toNum=group+1
    }
    var gb : Row=null
    var tempArray :Array[String]=null
    for (j<-fromNum until toNum){
      val MessageList= redisConnect.getRedisInfo(key+j)//
      println(key+j)
      for(i <- 0 until  MessageList.size()){
        try{
          tempArray = MessageList.get(i).split("\\$")
          gb= Row(tempArray(0).toInt, tempArray(1), tempArray(2), tempArray(3))
          list.add(gb)
        }
        catch {
          case e: Exception => println(e.getMessage)
        }
      }
    }

    sqlContext.createDataFrame(list, structMessageType).createOrReplaceTempView("tempWord")//创建dataframe
    println("====================initMessageTable end================================")
  }


}