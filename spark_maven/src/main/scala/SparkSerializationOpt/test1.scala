package SparkSerializationOpt

import com.esotericsoftware.kryo.Kryo
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.serializer.KryoRegistrator
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/11/2
  * \* Time: 13:16
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object test1 {
  //两个成员变量name和age，同时必须实现java.io.Serializable接口
  class MyClass1(val name: String, val age: Int) extends java.io.Serializable {
  }

  //两个成员变量name和age，同时必须实现java.io.Serializable接口
  class MyClass2(val name: String, val age: Int) extends java.io.Serializable {

  }

  //注册使用Kryo序列化的类，要求MyClass1和MyClass2必须实现java.io.Serializable
  class MyKryoRegistrator extends KryoRegistrator {
    override def registerClasses(kryo: Kryo): Unit = {
      kryo.register(classOf[MyClass1]);
      kryo.register(classOf[MyClass2]);
    }
  }
  def main(args: Array[String]): Unit = {
    //设置序列化器为KryoSerializer,也可以在配置文件中进行配置
//    System.setProperty("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//    System.setProperty("spark.kryo.registrator", "spark.examples.kryo.MyKryoRegistrator")
    Logger.getLogger("org").setLevel(Level.ERROR)

//    val sqlContext=SparkSession.builder().master("local[*]").appName("test").getOrCreate()
//    val sqlContext = SparkSession.builder()
//      .appName("FooBar")
//        .master("local[*]")
//      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//      .config("spark.kryoserializer.buffer.max", "2047m")
//      .config("spark.kryo.registrator","com.foo.bar.MyKryoRegistrator")
//      .config("spark.kryo.registrationRequired", "true")
//      .config("spark.network.timeout", "3600s")
//      .config("spark.driver.maxResultSize", "0")
//      .config("spark.rdd.compress", "true")
//      .config("spark.shuffle.spill", "true")
//      .getOrCreate()
val conf = new SparkConf()
    conf.setAppName("SparkKryoPrimitiveType")
    //这句是多于的，调用conf的registerKryoClasses时，已经设置了序列化方法
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    // Be strict about class registration
    ///如果一个要序列化的类没有进行Kryo注册，则强制Spark报错
    conf.set("spark.kryo.registrationRequired", "true")

    conf.setMaster("local[3]")
    val sc = new SparkContext(conf)
    val rdd =sc.parallelize(List(new MyClass1("Tom", 31), new MyClass1("Jack", 23), new MyClass1("Mary", 19)))
    val fileDir = "kryo" + System.currentTimeMillis()
    //将rdd中的对象通过kyro进行序列化，保存到fileDir目录中
    rdd.saveAsObjectFile(fileDir)
    //读取part-00000文件中的数据，对它进行反序列化，，得到对象集合rdd1
    val rdd1 = sc.objectFile[MyClass1](fileDir + "/" + "part-00000")


    rdd1.foreachPartition(iter => {
      while (iter.hasNext) {
        val objOfMyClass1 = iter.next();
        println(objOfMyClass1.name)
      }
    })

    sc.stop()
  }
}

