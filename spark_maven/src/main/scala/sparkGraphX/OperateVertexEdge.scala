package sparkGraphX

import org.apache.log4j.{Level, Logger}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/11/22
  * \* Time: 13:48
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object OperateVertexEdge {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("Test Graph").getOrCreate().sparkContext
    println("example=========================")
    // 顶点RDD[顶点的id,顶点的属性值]
    val users: RDD[(VertexId, (String, String))] =
      sc.parallelize(Array((3L, ("rxin", "student")), (7L, ("jgonzal", "postdoc")),
        (5L, ("franklin", "prof")), (2L, ("istoica", "prof"))))
    // 边RDD[起始点id,终点id，边的属性（边的标注,边的权重等）]
    val relationships: RDD[Edge[String]] =
      sc.parallelize(Array(Edge(3L, 7L, "collab"),    Edge(5L, 3L, "advisor"),
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
    println("=============")
    relationships.take(1).foreach(edge=>{
      println(edge.dstId)//7L
      println(edge.srcId)//3L
      println(edge.attr)//collab
      println("=============")










//      val edgeGroupedGraph:Graph[(String, Long), (Long, java.util.Date)] = graph2.groupEdges(merge = (e1, e2) => (e1._1 + e2._1, if(e1._2.getTime < e2._2.getTime) e1._2 else e2._2))
    })
  }
}

