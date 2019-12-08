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
  * \* Time: 15:38
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object GraphDegree {
  def main(args: Array[String]): Unit = {
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
    println("度的Reduce，统计度的最大值======================================================")
    val totalDegree=graph.degrees.reduce((a,b)=>max(a, b))//
    val inDegree=graph.inDegrees.reduce((a,b)=>max(a,b))//入度
    val outDegree=graph.outDegrees.reduce((a,b)=>max(a,b))//出度
    print("max total Degree = "+totalDegree)
    print("max in Degree = "+inDegree)
    print("max out Degree = "+outDegree)
    //小技巧：如何知道a和b的类型为(VertexId,Int)？
    //当你敲完graph.degrees.reduce((a,b)=>，再将鼠标点到a和b上查看，
    //就会发现a和b是(VertexId,Int)，当然reduce后的返回值也是(VertexId,Int)
    //这样就很清楚自己该如何定义max函数了

    //平均度
    println
    println("平均度=========================================================================")
    val sumOfDegree=graph.degrees.map(x=>(x._2.toLong)).reduce((a,b)=>a+b)
    val meanDegree=sumOfDegree.toDouble/graph.vertices.count().toDouble
    print("meanDegree "+meanDegree)
    println


  //    ------------------使用RDD自带的统计函数进行度分布分析--------
    println("使用RDD自带的统计函数进行度分布分析=============================================")
    //度的统计分析
    //最大，最小
  val degree2=graph.degrees.map(a=>(a._2,a._1))
    //graph.degrees是VertexRDD[Int],即（VertexID，Int）。其中Int为 定点vertexID的度
    //通过上面map调换成map(a=>(a._2,a._1)),即RDD[(Int,VetexId)]
    //这样下面就可以将度（Int）当作键值（key）来操作了，
    //包括下面的min，max，sortByKey，top等等，因为这些函数都是对第一个值也就是key操作的
    //max degree
    print("max degree = " + (degree2.max()._2,degree2.max()._1))
    println
    //min degree
    print("min degree =" +(degree2.min()._2,degree2.min()._1))
    println

    //top（N） degree"超级节点"
    print("top 3 degrees:\n")
    degree2.sortByKey(true, 1).top(3).foreach(x=>print(x._2,x._1))
    println
    /*输出结果：
     * max degree = (2,4)//（Vetext，degree）
     * min degree =（1,2)
     * top 3 degrees:
     * (2,4)(5,3)(3,3)
     */




  }
  def max(a:(VertexId,Int),b:(VertexId,Int)):(VertexId,Int)={
    if (a._2>b._2) a  else b }//度的Reduce，统计度的最大值



}

