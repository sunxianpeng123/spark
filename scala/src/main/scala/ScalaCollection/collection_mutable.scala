package ScalaCollection
import scala.collection.immutable.LongMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/8/2
  * \* Time: 15:17
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object collection_mutable {
  def main(args: Array[String]): Unit = {
    AnyRef//所有类型的基类，为anyref类型的键准备的映射表，开放地址法寻址，操作一般比hashMap快
    ArrayBuffer//内部用数组实现的缓冲区类，追加、更新、与随机访问的均摊时间复杂度为O(1)，头部插入和删除操作复杂度为O（logn）
    mutable.ArrayOps//java数组的包装类，实现了序列操作
    mutable.ArrayStack//数组实现的栈，比普通的栈速度快
    mutable.BitSet //非负整数集合，内存效率高，元素表示成比特数组，64位，最大元素个数决定内存占用量
    mutable.HashMap//基于散列表的的可变版本的映射
    mutable.HashSet//基于散列表的的可变版本的集合
//    mutable.HashTable
    mutable.ListMap//基于列表实现的的映射
    mutable.LinkedHashMap//基于散列表的映射，元素可以按插入顺序遍历
    mutable.LinkedHashSet//基于散列表的集合，元素可以按插入顺序遍历
    LongMap//键的类型全为Long，基于散列表实现的可变映射
    Map//map特征的可变版，
//    mutable.MultiMap$class//可变的映射，可以一个键对应多个值
    mutable.PriorityQueue//基于堆的，可变有限队列，对于类型为A的元素，必须存在隐含的Ording[A]的实例。
    mutable.Queue//可变的先进先出队列
    Seq//可变序列的trait，
    Set//
    mutable.SortedSet//可变集合的trait，包含一个可按特定排列顺序遍历元素的迭代器
    mutable.Stack//可变的后入先出的栈
    mutable.TreeSet//可变集合，底层用红黑树实现，操作复杂度O(logn)
    mutable.WeakHashMap//可变的散列映射，引用袁术是采用弱引用，当元素不再有强引用时，就会被删除，该类包装了weakhashMap
    mutable.WrappedArray//java数组的包装类，支持序列的操作，与AnyRef几乎完全相同



  }
}

