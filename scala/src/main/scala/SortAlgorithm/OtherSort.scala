package SortAlgorithm

import scala.collection.mutable.ArrayBuffer
import util.control.Breaks._
;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/9/25
  * \* Time: 10:25
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object OtherSort {
  def main(args: Array[String]): Unit = {
    val testData = new ArrayBuffer[Int]
    testData += 10;testData += 101;testData += 99;testData += 3;testData += 9;testData += 12;testData += 77;testData += 86;testData += 99;testData += 25
    println("testData = "+testData)
//    val selectionSortData=selectionSort(testData)
//    println("selectionSortData = "+selectionSortData)
//    val mySelectionSortData=mySelectionSort(testData)
//    println("mySelectionSortData = "+mySelectionSortData)

  }
  //归并排序
  /**
    * 归并排序采用递归实现，总体上有两个步骤（分解与归并）：先将待排序数组R[0...n-1]分解成n个长度为1的有序序列（其实是一个元素），
    * 将相邻的两个元素有序归并，得到n/2个长度为2的有序序列；然后将这些有序序列再次归并，得到n/4个长度为4的有序序列；如此反复进行下去，
    * 最后得到一个长度为n的有序序列。归并的具体步骤：比较arr[i]和arr[j],将较小值复制到新的数组new_arr[k],同时让i，j都加上1。如此循环下去，
    * 直到其中一个数组被复制完，再让另一个数组所剩余的元素（已排序）直接拼接到的new_arr的后面。
    * @param a
    * @param b
    * @return
    */
  def merge(a: List[Int], b: List[Int]): List[Int] = (a, b) match {
    case (Nil, _) => b case (_, Nil) => a
    case (x :: xs, y :: ys) => if (x <= y) x :: merge(xs, b)
    else y :: merge(a, ys)
  }
  def mergeSort(lst: List[Int]): List[Int] = {
    if (lst.length < 2) lst
    else {
      val (first, second) = lst.splitAt(lst.length / 2)
      merge(mergeSort(first), mergeSort(second)) } }
  //计数排序
  def Countingsort(inputData: ArrayBuffer[Int], k: Int): Array[Int] = {
    //k表示有所输入数字都介于0到k之间
    val temp = new Array[Int](k)
    // 临时存储区
    val outdata = new Array[Int](inputData.length)
    val len = temp.length
    for (i <- 0 until len) {
      // 初始化
      temp(i) = 0
    }
    for (i <- inputData.indices) {
      temp(inputData(i)) = temp(inputData(i)) + 1
    }
    for (i <- 1 until len) {
      temp(i) = temp(i) + temp(i - 1)
    } // 把输入数组中的元素放在输出数组中对应的位置上
    var n = inputData.length - 1
    while (n >= 0) { // 从后往前遍历
      outdata(temp(inputData(n)) - 1) = inputData(n)
      temp(inputData(n)) = temp(inputData(n)) - 1
      n = n - 1 }
    outdata
  }
  //桶排序
  def bucketsort(inputData: ArrayBuffer[Int], max: Int): ArrayBuffer[Int] = {
    var buckets = new Array[Int](max)
    for (i <- inputData.indices)
    //计数
    buckets(inputData(i)) = buckets(inputData(i)) + 1
    var j = 0
    for (i <- 0 until max)
      while (buckets(i) > 0) {
        inputData(j) = i
        j = j + 1
        buckets(i) = buckets(i) - 1
      }
    buckets = null
    inputData }
  /** 基数排序函数* B表示要排序的数组* d表示每一位数字的范围（这里是10进制数，有0~9一共10种情况） */
  def RadixSort(inputData: ArrayBuffer[Int], d: Int): ArrayBuffer[Int] = {
    //n用来表示当前排序的是第几位
    var n = 1 //hasNum用来表示数组中是否有至少一个数字存在第n位
    var hasNum = false /** 二维数组temp用来保存当前排序的数字* 第一维d表示一共有d个桶 * 第二维B.length表示每个桶最多可能存放B.length个数字*/
    val temp = Array.ofDim[Int](d, inputData.length)
    val order = new Array[Int](d)
    breakable {
      while (true) {
        //判断是否所有元素均无比更高位，因为第一遍一定要先排序一次，所以有n!=1的判断
        if (n != 1 && !hasNum)
          {
            break
          }
        hasNum = false //遍历要排序的数组，将其存入temp数组中（按照第n位上的数字将数字放入桶中）
        for (i <- inputData.indices) { val x = inputData(i) / (n * 10)
          if (x != 0) hasNum = true
          val lsd = x % 10
          temp(lsd)(order(lsd)) = inputData(i)
          order(lsd) = order(lsd) + 1 } //k用来将排序好的temp数组存入B数组（将桶中的数字倒出）
        var k = 0
        for (i <- 0 until d) {
          if (order(i) != 0) {
          var j = 0
            while (j < order(i)) {
              inputData(k) = temp(i)(j)
              k = k + 1
              j = j + 1
            }
          }
          order(i) = 0
        }
        n = n + 1
      }
    }
    inputData
  }
}



