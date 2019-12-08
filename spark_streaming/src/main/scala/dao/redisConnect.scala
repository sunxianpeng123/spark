package dao

import java.io.FileInputStream
import java.util.Properties

import redis.clients.jedis.{JedisPool, JedisPoolConfig}
import tools.PropertyUtil

object redisConnect {

  var JedisPool:JedisPool=null

  val RoomSourceGameHashKeyPre:String = "RM_SOGME_HS"


  var properties:Properties=null
  var path:String=null

  def init(): Unit ={
    getConnect()
  }

  def  getConnect() :JedisPool ={
//    properties= new Properties()
//    val filePath = "db_streaming_test.properties"
//    properties.load(new FileInputStream(filePath))
    val config =new JedisPoolConfig
    config.setTestOnBorrow(true)
    config.setTestOnReturn(true)
    print("ip:"+PropertyUtil.getString("redisIp"))
    JedisPool = new JedisPool(config,PropertyUtil.getString("redisIp"), PropertyUtil.getInt("redisPort",6379), 1000*60*60)

    JedisPool
  }

  //批量插入房间的分类
  /*
  def setRoomSourceGameKeyByList(retList:Array[(Int,String,String)]): Unit ={
    //房间分类对应的key   ANCHOROOM_SOURCEG_平台id_房间id
    val jedis = JedisPool.getResource
    val oneLint:Int = 2000//多少条作为一次插入
    val totalNum:Int = retList.length
    if(totalNum>0){
      val insertnums = Math.ceil( totalNum/oneLint.toFloat).toInt
      for(i <- 1 to insertnums){
        var itemstrBuff:mutable.ArrayBuffer[(String,String)] = new mutable.ArrayBuffer()
        for(j<- (0 + (i-1)* oneLint) until (oneLint+ (i-1)* oneLint) if (j<totalNum) ){
           val (a,b,c) = retList(j)
          itemstrBuff.append( (a + "_" + b,c))
        }
        if(itemstrBuff.length>0){
          jedis.hmset(RoomSourceGameHashKeyPre,   itemstrBuff.toMap)
        }
      }
    }

    val newmap = retList.map(u=>{
      val (a,b,c) = u
      (a + "_" + b,c)
    }).toMap
    jedis.hmset(RoomSourceGameHashKeyPre,   newmap)

  }*/

  def getSourceGameByROOM(keySearch:String): String ={
    val jedis = JedisPool.getResource
    val value =jedis.hget(RoomSourceGameHashKeyPre ,keySearch)
    //JedisPool.returnResource(jedis)
    value
  }
/*
  def getSourceGameByMultipleROOMs(keySearchs:Array[String]):List[String]={
    val jedis = JedisPool.getResource
    val value = jedis.hmget(RoomSourceGameHashKeyPre,keySearchs: _*).toList
    value
  }
  */
  def delSourdeGanemKey():Long ={
    val jedis = JedisPool.getResource
    val value =jedis.del(RoomSourceGameHashKeyPre)
    //JedisPool.returnResource(jedis)
    value
  }










  def test(): Unit ={
    val jedis = JedisPool.getResource
    for( i <- 1 to 7){
      jedis.lpush("testlists","qwert"+i)
      }

    //JedisPool.returnResource(jedis)

  }
  def getRedisInfo(key:String): java.util.List[String] ={
    val jedis = JedisPool.getResource
    val value =jedis.lrange(key,0,-1)
    //JedisPool.returnResource(jedis)
    value
  }

  def close(): Unit ={

    JedisPool.destroy()
  }





}