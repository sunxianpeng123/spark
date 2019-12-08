package ScalaCollection

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/8/2
  * \* Time: 13:54
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object collection_concurrent {
  def main(args: Array[String]): Unit = {
    import collection.concurrent.Map//extends collection.mutable.Map//支持线程安全的并发访问
    import collection.concurrent.TrieMap//实现了并发、无锁的三列数组，目的是支持可伸缩的并发删除和插入操作，提高内存使用效率


  }
}
