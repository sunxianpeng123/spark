package com.xiaohulu.pool

import java.util.Properties

import org.apache.commons.pool2.impl.{DefaultPooledObject, GenericObjectPool}
import org.apache.commons.pool2.{BaseObjectPool, BasePooledObjectFactory}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/5/21
  * \* Time: 21:37
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
class KafkaProxy(brokers:String){
//  存放配置文件
  private  val pros:Properties=new Properties()
  pros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,brokers);
  pros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer")
  pros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer")
  val kafkaProducer = new KafkaProducer[String,String](pros)

  def send(topic:String,key:String,value:String):Unit={
    kafkaProducer.send(new ProducerRecord[String,String](topic,key,value))
  }
  def send(topic:String,value:String):Unit={
    kafkaProducer.send(new ProducerRecord[String,String](topic,value))
  }
  def close():Unit={
    kafkaProducer.close()
  }
}

class KafkaProxyFactory(brokers:String) extends  BasePooledObjectFactory[KafkaProxy]{
//  创建实例
  override def create() = new KafkaProxy(brokers)

//将池中对象封装
//  override def wrap(t: KafkaProxy) =  BaseObjectPool[KafkaProxy]
  override def wrap(obj: KafkaProxy) =  new DefaultPooledObject[KafkaProxy](obj)
}

object KafkaPool {
//  声明池对象
  var kafkapool:GenericObjectPool[KafkaProxy]=null

  def apply(brokers: String): GenericObjectPool[KafkaProxy] ={
    if(kafkapool ==null){
      KafkaPool.synchronized{
        if (kafkapool==null){
          kafkapool =new GenericObjectPool[KafkaProxy](new KafkaProxyFactory(brokers))
        }
      }
    }
    kafkapool
  }
}
