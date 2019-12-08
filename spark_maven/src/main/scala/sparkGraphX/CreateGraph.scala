package sparkGraphX
import org.apache.log4j.{Level, Logger}

import org.apache.spark.graphx._
import org.apache.spark.sql.SparkSession
// To make some of the examples work we will also need RDD
import org.apache.spark.rdd.RDD

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/18
  * \* Time: 18:45
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object CreateGraph {
  def main(args:Array[String]){
    /**
      * 下面是一个简单的例子，说明如何建立一个属性图，
      */

    // Create the context
//    val sparkConf = new SparkConf().setAppName("myGraphPractice").setMaster("local[2]")
//    val sc=new SparkContext(sparkConf)
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sqlContext=SparkSession.builder().master("local[*]").appName("Test Graph").getOrCreate()
    val sc =sqlContext.sparkContext
    println("example=========================")
    // 顶点RDD[顶点的id,顶点的属性值]
    val users: RDD[(VertexId, (String, String))] =
      sc.parallelize(Array((3L, ("rxin", "student")), (7L, ("jgonzal", "postdoc")),
        (5L, ("franklin", "prof")), (2L, ("istoica", "prof"))))
    // 边RDD[起始点id,终点id，边的属性（边的标注,边的权重等）]
    val relationships: RDD[Edge[String]] =
      sc.parallelize(Array(Edge(3L, 7L, "collab"),Edge(5L, 3L, "advisor"),
        Edge(2L, 5L, "colleague"), Edge(5L, 7L, "pi")))
    // 默认（缺失）用户
    //Define a default user in case there are relationship with missing user
    val defaultUser = ("John Doe", "Missing")


    //使用RDDs建立一个Graph（有许多建立Graph的数据来源和方法，后面会详细介绍）
    val exampleGraph = Graph(users, relationships, defaultUser)
    println(exampleGraph)
    println(exampleGraph.edges)
    println(exampleGraph.vertices)
    exampleGraph.edges.foreach(println)

    /**
      * 那么建立一个图主要有哪些方法呢？我们先看图的定义：
      */
//    def apply[VD, ED](vertices: RDD[(VertexId, VD)], edges: RDD[Edge[ED]],defaultVertexAttr: VD = null):Graph[VD, ED]
//    def fromEdges[VD, ED](edges: RDD[Edge[ED]], defaultValue: VD): Graph[VD, ED]
//    def fromEdgeTuples[VD](rawEdges: RDD[(VertexId, VertexId)], defaultValue: VD, uniqueEdges: Option[PartitionStrategy] = None): Graph[VD, Int]

//    由上面的定义我们可以看到，GraphX主要有三种方法可以建立图：
//    （1）在构造图的时候，会自动使用apply方法，因此前面那个例子中实际上就是使用apply方法。相当于Java/C++语言的构造函数。
//    有三个参数，分别是：vertices: RDD[(VertexId, VD)], edges: RDD[Edge[ED]], defaultVertexAttr: VD = null)，前两个必须有，
//    最后一个可选择。“顶点“和”边“的RDD来自不同的数据源，与Spark中其他RDD的建立并没有区别。
//    这里再举读取文件，产生RDD，然后利用RDD建立图的例子：
    println("defination==================================")
    println("自动使用apply方法建立图===========")
    //读入数据文件
    val articles: RDD[String] = sc.textFile("data/graphx/users_t.txt")
    val links: RDD[String] = sc.textFile("data/graphx/followers.txt")
    //装载“顶点”和“边”RDD
    val vertices = articles.map { line =>
      val fields = line.split(' ')
      (fields(0).toLong, fields(1))
    }//注意第一列为vertexId，必须为Long，第二列为顶点属性，可以为任意类型，包括Map等序列。

      val edges = links.map { line =>
      val fields = line.split(' ')
      Edge(fields(0).toLong, fields(1).toLong, 1L)//起始点ID必须为Long，最后一个是属性，可以为任意类型
    }
    //建立图
    val definationGraph = Graph(vertices, edges, "").persist()//自动使用apply方法建立图
    definationGraph.vertices.foreach(println)
    println("*************")
    definationGraph.edges.foreach(println)
//    （2）Graph.fromEdges方法:这种方法相对而言最为简单，只是由”边”RDD建立图，由边RDD中出现所有“顶点”（无论是起始点src还是终点dst）自动产生顶点vertextId，顶点的属性将被设置为一个默认值1L。
//    Graph.fromEdges allows creating a graph from only an RDD of edges, automatically creating any vertices mentioned by edges and assigning them the default value.
//      举例如下：
    println("Graph.fromEdges=============")
    //读入数据文件
    val records: RDD[String] = sc.textFile("data/graphx/weibo_fromEdges.txt")
    //微博数据：000000261066,  郜振博585, 3044070630 ,redashuaicheng, 1929305865, 1994,229,3472,male,first
    // 第三列是粉丝Id：3044070630,第五列是用户Id：1929305865
    val followers=records.map {case x => val fields=x.split(",")
      Edge(fields(2).toLong, fields(4).toLong,1L)
    }
    val fromEdgesGraph=Graph.fromEdges(followers, 1L)
    fromEdgesGraph.edges.foreach(println)
    println("*************")
    fromEdgesGraph.vertices.foreach(println)

    println("Graph.fromEdgeTuples====================")
//Graph.fromEdgeTuples方法
    /*
     Graph.fromEdgeTuples allows creating a graph from only an RDD of edge tuples, assigning the edges the value 1,
     and automatically creating any vertices mentioned by edges and assigning them the default value. It also supports
     deduplicating the edges; to deduplicate, pass Some of a PartitionStrategy as the uniqueEdges parameter (for example,
     uniqueEdges = Some(PartitionStrategy.RandomVertexCut)). A partition strategy is necessary to colocate identical
     edges on the same partition so they can be deduplicated.
     */
//    val data = sqlContext.sql("select year, trade_flow, reporter_iso, partner_iso, sum(trade_value_us) from comtrade.annual_hs where length(commodity_code)='2' " +
//      "and not partner_iso='WLD' group by year, trade_flow, reporter_iso, partner_iso").collect()
//    val data_2010 = data.filter(line => line(0)==2010)
//    val couples = data_2010.map(line=>(line(2),line(3))) //country to country
//    val idMap = sc.broadcast(couples // -> Array[(Any, Any)]
//      // Make sure we use String not Any returned from Row.apply
//      // And convert to Seq so we can flatten results
//      .flatMap{case (x: String, y: String) => Seq(x, y)} // -> Array[String]
//      // Get different keys
//      .distinct // -> Array[String]
//      // Create (key, value) pairs
//      .zipWithIndex  // -> Array[(String, Int)]
//      // Convert values to Long so we can use it as a VertexId
//      .map{case (k, v) => (k, v.toLong)}  // -> Array[(String, Long)]
//      // Create map
//      .toMap
//         ) // -> Map[String,Long]
//    val edges1: RDD[(VertexId, VertexId)] = sc.parallelize(couples
//      .map{case (x: String, y: String) => (idMap.value(x), idMap.value(y))}
//    )
//    val graph = Graph.fromEdgeTuples(edges1, 1)

    println("GraphLoader====================")
//    （4）GraphLoader.edgeListFile建立图的基本结构,然后Join属性
//    (a)首先建立图的基本结构：
//    利用GraphLoader.edgeListFile函数从边List文件中建立图的基本结构（所有“顶点”+“边”），且顶点和边的属性都默认为1。
//    （b)然后读取属性文件，获得RDD后和（1）中得到的基本结构图join在一起，就可以组合成完整的属性图。
    val GraphLoaderGraph=GraphLoader.edgeListFile(sc, "data/graphx/followers.txt")
    //文件的格式如下：
    //2 1
    //4 1
    //1 2  依次为第一个顶点和第二个顶点
    GraphLoaderGraph.vertices.foreach(println)
    println("********")
    GraphLoaderGraph.edges.foreach(println)



  }
}
