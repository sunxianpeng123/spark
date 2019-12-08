package learn_rdd

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
  * Created by Dell on 2017/6/14.
  */
object testPairRdd {
  def main(args: Array[String]): Unit = {
    val conf =new SparkConf().setMaster("local").setAppName("myapp")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")
//    单个Rdd
    var lines=sc.parallelize(List("this is a Test of pairRdd"))
    val pairRdd=lines.map(x=>(x,1))
    pairRdd.foreach(println)
//
    println("reduceByKey::")
    val rdd = sc.parallelize(List((1,2),(3,4),(3,6)))
    val result1=rdd.reduceByKey((x,y)=>x+y)
    result1.foreach(println)
//
    println("groupByKey::")
    val result2=rdd.groupByKey()
    result2.foreach(println)
//
    println("mapvalues::")
    val result3=rdd.mapValues(x=>x+1)
    result3.foreach(println)
//
    println("flatMapValues::")
    val result4=rdd.flatMapValues(x=>(x to 5))//flatMapValues应用于元素为KV对的RDD中Value,从当前values值开始，按照传入函数将value映射为一系列值，切映射后的值均再与当前的键组成新的kv对。
    result4.foreach(println)
//
    println("keys::")
    val result5=rdd.keys
    result5.foreach(println)
//
    println("values::")
    val result6=rdd.values
//
    println("sortByKey::")
    val result7=rdd.sortByKey()
    result7.foreach(println)
//
    println("过滤操作::")
    println("\t 对value做控制，key不加限制条件::")
    val result8=rdd.filter{case(x,y) => y%3 == 0}
    result8.foreach(println)
    println("\t 对key做控制，value不控制::")
    val result9=rdd.filter{ case (x,y) => x < 3}
    result9.foreach(println)
//
    println("聚合操作：：")
    println("\t 使用reduceByKey()和mapValues()计算每个键对应的平均值::")
    val wordRdd = sc.parallelize(List(Tuple2("panda",0),Tuple2("pink",3),Tuple2("pirate",3),Tuple2("panda",1),Tuple2("pink",4)))
    val result10=wordRdd.mapValues(x=>(x,1)).reduceByKey((x,y)=>(x._1+y._1,x._2+y._2))
    result10.foreach(println)
    println("\t 实现经典的分布式单词计数问题（使用flatMap() 来生成以单词为键，以数字1为值的pair RDD）")
    val sent = sc.parallelize(List("i am thinkgamer, i love cyan"))
    val words =sent.flatMap(line=> line.split(" "))
    val result11=words.map(x => (x,1)).reduceByKey{(x,y)=>x+y}
    result11.foreach(println)
    println("\t 实现经典的分布式单词计数问题（使用countByValue更快的实现单词计数）::")
    val result12=sent.flatMap(line => line.split(" ")).countByValue()
    result12.foreach(println)
//
    println("行动操作：：")
    println("countByKey")
    val result13=rdd.countByKey()
    result13.foreach(println)
    println("collectAsMap")
    val result14=rdd.collectAsMap()
    println(result14)
    result14.foreach(println)
    println("lookup")
    val result15=rdd.lookup(1)
    result15.foreach(println)
//
    println("获取RDD的分区方式::")
    val pairs=sc.parallelize(List((1,1),(2,2),(3,3)))
    println(pairs.partitioner)
    val partitioned=pairs.partitionBy(new HashPartitioner(2))//构造两个分区，partitionBy是一个转化操作，该函数根据partitioner函数生成新的ShuffleRDD，将原RDD重新分区。不会改变原来的rdd，会生成一个新的rdd
    partitioned.persist()//持久化
    println(partitioned.partitioner)
    partitioned.unpersist()
  }
}
