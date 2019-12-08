package ScalaCollection
import collection.immutable._


/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/8/2
  * \* Time: 14:04
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object collection_immutable {
  """
    |所有的集合类在scala.collection 包中，或者他的子包中，分为mutable，immutable以及generic
    |scala默认的集合包是不可变的
    |如果你想用一个可变的Set，你需要导入collection.mutable.Set
    |如果想两个都引用，又想简单写，有个办法
  """
  def main(args: Array[String]): Unit = {
    //提供单线程操作，由于是类型不可变的所以是线程安全的
    //常用类型：
    BitSet//非负整数集合，内存效率高，元素表示成比特数组，64位，最大元素个数决定内存占用量
    val bits = BitSet(1, 2, 3)
    println(bits)
    HashMap//字典散列实现的映射表
    HashSet//字典散列实现集合
    List//
    ListMap//列表实现不可变映射表
    ListSet//列表实现的不可变集合
    Map
    Nil//表示空列表
    Range//
    val range =Range(1,10,3)
    println(range)
    NumericRange//range的推广类版本，适用于任意完整的类型，
    val numericRange = NumericRange(1,10,3)
    println(numericRange)
    Queue//不可变的先入后出队列
    Seq
    Set
    SortedMap//为不可变映射表定义的trait，包含特定排列顺序遍历元素的迭代器
    SortedSet//为不可变集合表定义的trait， 特定排列顺序遍历元素的迭代器
    Stack//不可变的后入先出栈
    Stream//对元素惰性求值的列表，可以支持拥有无限个潜在元素的序列
    TreeMap//不可变映射表，底层用红黑树实现，操作复杂度O(logn)
    TreeSet//不可变集合，底层用红黑树实现，操作复杂度O(logn)
    Vector//不可变，支持下标的序列的默认实现

  }
}

