package sparkRDD.TransformOperations

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Dell on 2017/6/14.
  */
object SingleRDD {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    //转化
    val conf =new SparkConf().setMaster("local").setAppName("myapp")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")
    val inputRDD =sc.textFile("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\README.md")
//
    println("filter::")
    val hadoopRdd=inputRDD.filter(line=>line.contains("Hadoop"))
    hadoopRdd.take(10).foreach(println)
//  常见转化和行动操作
    println("map--take--collect--foreach--mkString::")
    val input = sc.parallelize(List(1,2,3,4))
    val result= input.map(x => x*x)
    result.take(4).foreach(println)
    println(result.collect().mkString(","))
//
    val lines=sc.parallelize(List("hello world","hi"))
    val words=lines.flatMap(line =>line.split(" "))
    println(words.collect().mkString("--"))
    println(words.first())
//map 与flatmap区别
    println("map 与flatmap区别::")
    val sen=List("coffee panda","happy panda","happiest panda party")
    val senRdd=sc.parallelize(sen)
    val mapRdd=senRdd.map(line =>line.split(" "))
    val flatmapRdd=senRdd.flatMap(line=>line.split(" "))
    println("map::")
    println(mapRdd.first().take(10).foreach(println))//map把每句话分别分成一个列表
    println("flatmap::")//flatmap把每个元素先操作，然后将操作后的合并在一起
    println(flatmapRdd.first())
//行动
    //val input = sc.parallelize(List(1,2,3,4))
    println("sum::")
    val sum=input.reduce((x,y)=>x+y)
    println(sum.toDouble)
//求平均值
    val avg_pre=input.aggregate((0,0))(//首先，初始值是(0,0)，这个值在后面2步会用到。然后，(acc,number) => (acc._1 + number, acc._2 + 1)，number即是函数定义中的T，这里即是List中的元素。
      // 所以acc._1 + number, acc._2 + 1的过程如下。结果即是(45,9)。这里演示的是单线程计算过程，实际Spark执行中是分布式计算，可能会把List分成多个分区，假如3个，p1(1,2,3,4)，p2(5,6,7,8)，
      // p3(9)，经过计算各分区的的结果（10,4），（26,4），（9,1），这样，执行(par1,par2) => (par1._1 + par2._1, par1._2 + par2._2)就是（10+26+9,4+4+1）即（45,9）.再求平均值就简单了。
      (acc,value)=>(acc._1+value,acc._2+1),
      (acc1,acc2)=>(acc1._1+acc2._1,acc1._2+acc2._2)
    )
    val avg=avg_pre._1/avg_pre._2.toDouble
    println(avg)
//    持久化
//    2.3 非RDD典型实例
//    1.Accumulator
//    Accumulator是spark提供的累加器，顾名思义，该变量只能够增加。 只有driver能获取到Accumulator的值（使用value
    // 方法），Task只能对其做增加操作（使用 +=）。你也可以在为Accumulator命名（不支持Python），
    // 这样就会在spark web ui中显示，可以帮助你了解程序运行的情况。用于监控和调试，记录符合某类特征的数据数据等。
    println("Accumulator::")
    val assum=sc.accumulator(0,"test_accum")
    val assumRdd=sc.parallelize(1 to 10 ,5)
    assumRdd.foreach(x=>assum+=1)
    println(assum.value)
  }
}

