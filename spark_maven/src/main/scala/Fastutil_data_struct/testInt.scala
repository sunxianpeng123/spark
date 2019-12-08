package Fastutil_data_struct

import it.unimi.dsi.fastutil.ints.{Int2BooleanArrayMap, IntArrayList, IntBigArrayBigList, IntLinkedOpenHashSet}

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/8/18
  * \* Time: 14:09
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object testInt {
  """
    |fastutil以存储的元素类型来划分package，每个package下都有丰富的Class。
    |如：
    |   List、BigList、Map、SortedMap、Set、Stack、Iterator......
    |
    |    it.unimi.dsi.fastutil.booleans
    |    it.unimi.dsi.fastutil.bytes
    |    it.unimi.dsi.fastutil.chars
    |    it.unimi.dsi.fastutil.doubles
    |    it.unimi.dsi.fastutil.floats
    |    it.unimi.dsi.fastutil.ints
    |    it.unimi.dsi.fastutil.io
    |    it.unimi.dsi.fastutil.longs
    |    it.unimi.dsi.fastutil.objects
    |    it.unimi.dsi.fastutil.shorts
  """.stripMargin
  def main(args: Array[String]): Unit = {
    println("//===========IntList")
    val list =new IntArrayList()
    0.until(2).foreach(i=>{
      list.add(i)
    })
    println(list)
    println("//取值")
    val v=list.get(0)
    println(v)
    println("//转成数组")
    val arr =list.toIntArray()
    println(arr.length)
    println("   //遍历")
    val iter=list.iterator()
    while (iter.hasNext){
      println(iter.nextInt())
    }
    println("//===========Int2BooleanMap ")
    println("  //===========Int2BooleanMap  ")
    val map =new Int2BooleanArrayMap()
    map.put(1,true)
    map.put(2,false)
    println("======//取值  ")
    println(map.get(1))
    println(map.get(2))
    println("//===========IntBigList")
    val bigList=new IntBigArrayBigList()
    bigList.add(1)
    bigList.add(2)
    val size =bigList.size64()
    println("//取值  ")
    println(bigList.getInt(0.toLong))
    val s =new IntLinkedOpenHashSet(Array (4,3,2,1))
    println(s)
    println("==========//获取第一个元素  ")
    println(s.firstInt())
    println("==========//获取最后一个元素   ")
    println(s.lastInt())
    println("==========//判断是否包含一个元素   ")
    println(s.contains(5))
  }
}

