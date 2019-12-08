package redis

import java.io.FileInputStream
import java.util.Properties

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis._

object redisConnect {

  var JedisPool:JedisPool=null
  var properties:Properties=null
  var path:String=null
  def init(): Unit ={
    getConnect()
  }


  def  getConnect() :JedisPool ={
    properties= new Properties()
    path =  Thread.currentThread().getContextClassLoader.getResource("db.properties").getPath
    properties.load(new FileInputStream(path))
    println("###########REDISIP:"+properties.getProperty("redisIp"));
    JedisPool = new JedisPool(new GenericObjectPoolConfig(), properties.getProperty("redisIp"), properties.getProperty("redisPort").toInt, 1000*60*60)
    JedisPool
  }

  def close(): Unit ={

    JedisPool.destroy()
  }

  def test(): Unit ={
    val jedis = JedisPool.getResource
    for( i <- 1 to 100){
      jedis.lpush("test","hahahahaah"+i)
    }

    JedisPool.returnResource(jedis)

  }


  def getRedisInfo(key:String): java.util.List[String] ={
    val jedis = JedisPool.getResource
     val value =jedis.lrange(key,0,-1)

    JedisPool.returnResource(jedis)
    value
  }






}