package sparkGraphX
import org.apache.log4j.{Level, Logger}
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/19
  * \* Time: 13:11
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object GraphOperationMap {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc=SparkSession.builder().master("local[*]").appName("Test Graph").getOrCreate().sparkContext
//    Spark中图有以下三种视图可以访问，分别通过graph.vertices，graph.edges，graph.triplets来访问。
//假设graph顶点属性(String,Int)-(name,age),边有一个权重（int)
//读入数据文件
  val articles: RDD[String] = sc.textFile("data/graphx/users_t.txt")
    val links: RDD[String] = sc.textFile("data/graphx/followers.txt")
    //装载“顶点”和“边”RDD
    val vertices = articles.map { line =>
      val fields = line.split(' ')
      (fields(0).toLong, (fields(1),1))
    }//注意第一列为vertexId，必须为Long，第二列为顶点属性，可以为任意类型，包括Map等序列。

    val edges = links.map { line =>
      val fields = line.split(' ')
      Edge(fields(0).toLong, fields(1).toLong, 1L)//起始点ID必须为Long，最后一个是属性，可以为任意类型
    }
//    //建立图
//    val definationGraph = Graph(vertices, edges, "").persist()//自动使用apply方法建立图
    println("在Scala语言中，可以用case语句进行形式简单、功能强大的模式匹配=============")
    val graph = Graph(vertices, edges)
//    用case匹配可以很方便访问顶点和边的属性及id
    graph.vertices.map{
      case (id,(name,age))=> //利用case进行匹配
        (age,name)//可以在这里加上自己想要的任何转换
    } take(1) foreach(println)
    println("*********")
    graph.edges.map{
      case Edge(srcid,dstid,weight)=> //利用case进行匹配
        (dstid,weight*0.01)//可以在这里加上自己想要的任何转换
    }take 1 foreach(println)

    println("也可以通过下标访问==================")
    graph.vertices.map{
      v=>(v._1,v._2._1,v._2._2)//v._1,v._2._1,v._2._2分别对应Id，name，age
    }.take(1).foreach(println)
    println("*********")
    graph.edges.map {
      e=>(e.attr,e.srcId,e.dstId)
    }.take(1).foreach(println)
    println("*********")
    graph.triplets.map{
      triplet=>(triplet.srcAttr._1,triplet.dstAttr._2,triplet.srcId,triplet.dstId)
    }.take(1).foreach(println)
//    可以不用graph.vertices先提取顶点再map的方法，也可以通过graph.mapVertices直接对顶点进行map，
//    返回是相同结构的另一个Graph，访问属性的方法和上述方法是一模一样的。如下：
    println("mapVertices=========================")
    graph.mapVertices{
      case (id,(name,age))=> //利用case进行匹配
        (age,name)//可以在这里加上自己想要的任何转换
    }.edges.take(1) foreach println
    println("================")

    graph.mapVertices { case (vid, _) => vid }.vertices.foreach(println)


    graph.mapEdges(e=>(e.attr,e.srcId,e.dstId)).edges.take(1) foreach println

    graph.mapTriplets(triplet=>(triplet.srcAttr._1)).triplets take 1 foreach println

  }
}

