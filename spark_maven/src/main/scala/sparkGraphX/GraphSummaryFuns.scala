package sparkGraphX

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/19
  * \* Time: 13:48
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object GraphSummaryFuns {
  def main(args: Array[String]): Unit = {
//    // Information about the Graph
//    //图的基本信息统计
//    ===================================================================
//    val numEdges: Long
//    val numVertices: Long
//    val inDegrees: VertexRDD[Int]
//    val outDegrees: VertexRDD[Int]
//    val degrees: VertexRDD[Int]
//
//    // Views of the graph as collections
//    // 图的三种视图
//    =============================================================
//    val vertices: VertexRDD[VD]
//    val edges: EdgeRDD[ED]
//    val triplets: RDD[EdgeTriplet[VD, ED]]
//
//    // Functions for caching graphs ==================================================================
//    def persist(newLevel: StorageLevel = StorageLevel.MEMORY_ONLY): Graph[VD, ED]
//    def cache(): Graph[VD, ED]
//    def unpersistVertices(blocking: Boolean = true): Graph[VD, ED]
//    // Change the partitioning heuristic  ============================================================
//    def partitionBy(partitionStrategy: PartitionStrategy): Graph[VD, ED]
//
//    // Transform vertex and edge attributes
//    // 基本的转换操作
//    ==========================================================
//    def mapVertices[VD2](map: (VertexID, VD) => VD2): Graph[VD2, ED]
//    def mapEdges[ED2](map: Edge[ED] => ED2): Graph[VD, ED2]
//    def mapEdges[ED2](map: (PartitionID, Iterator[Edge[ED]]) => Iterator[ED2]): Graph[VD, ED2]
//    def mapTriplets[ED2](map: EdgeTriplet[VD, ED] => ED2): Graph[VD, ED2]
//    def mapTriplets[ED2](map: (PartitionID, Iterator[EdgeTriplet[VD, ED]]) => Iterator[ED2])
//    : Graph[VD, ED2]
//
//    // Modify the graph structure
//    //图的结构操作（仅给出四种基本的操作，子图提取是比较重要的操作）
//    ====================================================================
//    def reverse: Graph[VD, ED]
//    def subgraph(
//                  epred: EdgeTriplet[VD,ED] => Boolean = (x => true),
//                  vpred: (VertexID, VD) => Boolean = ((v, d) => true))
//    : Graph[VD, ED]
//    def mask[VD2, ED2](other: Graph[VD2, ED2]): Graph[VD, ED]
//    def groupEdges(merge: (ED, ED) => ED): Graph[VD, ED]
//
//    // Join RDDs with the graph
//    // 两种聚合方式，可以完成各种图的聚合操作  ======================================================================
//    def joinVertices[U](table: RDD[(VertexID, U)])(mapFunc: (VertexID, VD, U) => VD): Graph[VD, ED]
//    def outerJoinVertices[U, VD2](other: RDD[(VertexID, U)])
//                                 (mapFunc: (VertexID, VD, Option[U]) => VD2)
//
//    // Aggregate information about adjacent triplets
//    //图的邻边信息聚合，collectNeighborIds都是效率不高的操作，优先使用aggregateMessages，这也是GraphX最重要的操作之一。
//    =================================================
//    def collectNeighborIds(edgeDirection: EdgeDirection): VertexRDD[Array[VertexID]]
//    def collectNeighbors(edgeDirection: EdgeDirection): VertexRDD[Array[(VertexID, VD)]]
//    def aggregateMessages[Msg: ClassTag](
//                                          sendMsg: EdgeContext[VD, ED, Msg] => Unit,
//                                          mergeMsg: (Msg, Msg) => Msg,
//                                          tripletFields: TripletFields = TripletFields.All)
//    : VertexRDD[A]
//
//    // Iterative graph-parallel computation ==========================================================
//    def pregel[A](initialMsg: A, maxIterations: Int, activeDirection: EdgeDirection)(
//      vprog: (VertexID, VD, A) => VD,
//      sendMsg: EdgeTriplet[VD, ED] => Iterator[(VertexID,A)],
//      mergeMsg: (A, A) => A)
//    : Graph[VD, ED]
//
//    // Basic graph algorithms
//    //图的算法API（目前给出了三类四个API）  ========================================================================
//    def pageRank(tol: Double, resetProb: Double = 0.15): Graph[Double, Double]
//    def connectedComponents(): Graph[VertexID, ED]
//    def triangleCount(): Graph[Int, ED]
//    def stronglyConnectedComponents(numIter: Int): Graph[VertexID, ED]

  }
}

