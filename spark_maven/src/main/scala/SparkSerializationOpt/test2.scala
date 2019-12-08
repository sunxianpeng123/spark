package SparkSerializationOpt

import java.io.ByteArrayOutputStream

import com.esotericsoftware.kryo.io.Input
import org.apache.hadoop.io.{BytesWritable, NullWritable}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.{SparkConf, SparkContext}

import scala.reflect.ClassTag

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/11/2
  * \* Time: 14:20
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object test2 {

  class Person(val name: String)

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
//    if (args.length < 1) {
//      println("Please provide output path")
//      return
//    }
//    val outputPath = args(0)
    val outputPath="testkryo/"

    val conf = new SparkConf().setMaster("local").setAppName("kryoexample")// 创建SparkConf对象。
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")// 设置序列化器为KryoSerializer。
    conf.registerKryoClasses(Array(classOf[Person]))// 注册要序列化的自定义类型。
    val sc = new SparkContext(conf)

    //create some dummy data
    val personList = 1 to 10000 map (value => new Person(value + ""))
    val personRDD = sc.makeRDD(personList)

    saveAsObjectFile(personRDD, outputPath)
    val rdd = objectFile[Person](sc, outputPath)
    println(rdd.map(person => person.name).collect().toList)

  }

  def saveAsObjectFile[T: ClassTag](rdd: RDD[T], path: String) {
    val kryoSerializer = new KryoSerializer(rdd.context.getConf)

    rdd.mapPartitions(iter => iter.grouped(10)
      .map(_.toArray))
      .map(splitArray => {
        //initializes kyro and calls your registrator class
        val kryo = kryoSerializer.newKryo()

        //convert data to bytes
        val bao = new ByteArrayOutputStream()
        val output = kryoSerializer.newKryoOutput()
        output.setOutputStream(bao)
        kryo.writeClassAndObject(output, splitArray)
        output.close()

        // We are ignoring key field of sequence file
        val byteWritable = new BytesWritable(bao.toByteArray)
        (NullWritable.get(), byteWritable)
      }).saveAsSequenceFile(path)

  }

  def objectFile[T](sc: SparkContext, path: String, minPartitions: Int = 1)
                   (implicit ct: ClassTag[T]) = {
    val kryoSerializer = new KryoSerializer(sc.getConf)
    sc.sequenceFile(path, classOf[NullWritable], classOf[BytesWritable],
      minPartitions)
      .flatMap(x => {
        val kryo = kryoSerializer.newKryo()
        val input = new Input()
        input.setBuffer(x._2.getBytes)
        val data = kryo.readClassAndObject(input)
        val dataObject = data.asInstanceOf[Array[T]]
        dataObject
      })
  }

}
