package sparkGraphX

import org.apache.log4j.{Level, Logger}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/20
  * \* Time: 10:19
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object GraphSubGraph {
  /**
    * Structural Operators
    * Spark2.0版本中，仅仅有四种最基本的结构操作，未来将开发更多的结构操作。
    * class Graph[VD, ED] {
    * def reverse: Graph[VD, ED]
    * def subgraph(epred: EdgeTriplet[VD,ED] => Boolean,vpred: (VertexId, VD) => Boolean): Graph[VD, ED]
    * def mask[VD2, ED2](other: Graph[VD2, ED2]): Graph[VD, ED]
    * def groupEdges(merge: (ED, ED) => ED): Graph[VD,ED]
}
    * @param args
    */
  def main(args: Array[String]): Unit = {
//    Spark API–subgraph利用EdgeTriplet（epred）或/和顶点（vpred）满足一定条件，来提取子图。
//     利用这个操作可以使顶点和边被限制在感兴趣的范围内，比如删除失效的链接。
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("Test Graph").getOrCreate().sparkContext
    // 顶点RDD[顶点的id,顶点的属性值]
    //假设graph有如下的顶点和边 顶点RDD(id,(name,age) 边上有一个Int权重（属性）
//    (4,(David,42))(6,(Fran,50))(2,(Bob,27)) (1,(Alice,28))(3,(Charlie,65))(5,(Ed,55))
//    Edge(5,3,8)Edge(2,1,7)Edge(3,2,4) Edge(5,6,3)Edge(3,6,3)
    val vertices: RDD[(Long, (String, Int))] = sc.parallelize(Seq((4L, ("David", 42)), (6L, ("Fran", 50)),
        (2L, ("Bob", 27)), (1L, ("Alice", 28)),(3,("Charlie",65)),(5,("Ed",55))))
    // 边RDD[起始点id,终点id，边的属性（边的标注,边的权重等）]
    val edges: RDD[Edge[Int]] = sc.parallelize(Array( Edge(5,3,8),Edge(2,1,7),Edge(3,2,4) ,Edge(5,6,3),Edge(3,6,3)))
    val graph = Graph(vertices, edges)
    graph.vertices.foreach(println)
    println("方法1，对顶点进行操作==============================================================================")
    //可以使用以下三种操作方法获取满足条件的子图
    //方法1，对顶点进行操作
    val subGraph1=graph.subgraph(vpred=(id,attr)=>attr._2>30)
    //vpred=(id,attr)=>attr._2>30 顶点vpred第二个属性（age）>30岁
    subGraph1.vertices.foreach(print)
    println
    subGraph1.edges.foreach {print}
    println
//    输出结果：
//    顶点：(4,(David,42))(6,(Fran,50))(3,(Charlie,65))(5,(Ed,55))
//    边：Edge(3,6,3)Edge(5,3,8)Edge(5,6,3)

    println("方法2--对EdgeTriplet进行操作=========================================================================")
    //方法2--对EdgeTriplet进行操作
    val subGraph2_1=graph.subgraph(epred=>epred.attr>2)
    //epred（边）的属性（权重）大于2
    subGraph2_1.vertices.foreach(print)
    println
    subGraph2_1.edges.foreach {print}
    println
//    输出结果：
//    顶点：(4,(David,42))(6,(Fran,50))(2,(Bob,27))(1,(Alice,28)) (3,(Charlie,65))(5,(Ed,55))
//    边：Edge(5,3,8)Edge(5,6,3)Edge(2,1,7)Edge(3,2,4) Edge(3,6,3)
    //也可以定义如下的操作
    println("***********************************************************************")
    val subGraph2_2=graph.subgraph(epred=>epred.srcAttr._2 < epred.dstAttr._2)
    //起始顶点的年龄小于终点顶点年龄
//    顶点：1,(Alice,28))(4,(David,42))(3,(Charlie,65))(6,(Fran,50)) (2,(Bob,27))(5,(Ed,55))
//    边 ：Edge(5,3,8)Edge(2,1,7)Edge(2,4,2)
    subGraph2_2.vertices.foreach(print)
    println
    subGraph2_2.edges.foreach {print}
    println

    //方法3--对顶点和边Triplet两种同时操作“，”号隔开epred和vpred
    val subGraph3=graph.subgraph(epred=>epred.attr>3,vpred=(id,attr)=>attr._2>30)
//    输出结果：
//    顶点：(3,(Charlie,65))(5,(Ed,55))(4,(David,42))(6,(Fran,50))
//    边：Edge(5,3,8)
    subGraph3.vertices.foreach(print)
    println
    subGraph3.edges.foreach {print}
    println

  }



}

