package Fastutil_data_struct

import it.unimi.dsi.fastutil.longs.{Long2IntAVLTreeMap, Long2IntArrayMap}
import org.apache.parquet.it.unimi.dsi.fastutil.longs.Long2IntSortedMap

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/8/18
  * \* Time: 19:01
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object testLong {
  def main(args: Array[String]): Unit = {
    val  m =new Long2IntAVLTreeMap()
    m.put( 1, 5 );
    m.put( 2, 6 );
    m.put( 3, 7 );
    m.put( 1000000000L, 10 );
    println(m.get(1))
println("    //当查找不到的时候，默认返回0")
    println(m.get(4))
  println("  //设置默认返回值")
    m.defaultReturnValue(-1)
    println(m.get(4))

    println("//遍历Map")
    val key1=m.keySet().iterator()
    var s =0L
    while (key1.hasNext){
      s += key1.nextLong()
    }
    println(s)
   println(" //获取Key值小于4的子Map")
    val m1 = m.headMap(4)
    val key2 =m1.keySet().iterator()
    while (key2.hasNext){
      println(key2.nextLong())
    }
  }
}

