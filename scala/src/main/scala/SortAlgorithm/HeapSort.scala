package SortAlgorithm

import scala.collection.mutable.ArrayBuffer

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/9/25
  * \* Time: 10:25
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object HeapSort {
  def main(args: Array[String]): Unit = {
    val testData = new ArrayBuffer[Int]
    testData += 10;testData += 101;testData += 99;testData += 3;testData += 9;testData += 12;testData += 77;testData += 86;testData += 99;testData += 25
    println("testData = "+testData)
    val heapSortData=heapSort(testData)
    println("heapSortData = "+heapSortData)

  }
  /*函数功能：堆化*/
  def heapify(inputData: ArrayBuffer[Int], m: Int, j: Int): Unit = {
    var i = m
    var k = 2 * i
    val x = inputData(i)
    import util.control.Breaks._
    breakable {
      //调用循环终止break方法
      while (k <= j) {
        if (k < j)
          if (inputData(k) < inputData(k + 1)) k = k + 1
        if (x >= inputData(k))
          break
        else {
          inputData(i) = inputData(k)
          i = k
          k = 2 * i
        }
      }
    }
    inputData(i) = x
  }

  /*函数功能：堆排序*/
  def heapSort(inputData: ArrayBuffer[Int]): ArrayBuffer[Int] = {
    var i = inputData.length / 2
    while (i >= 1) {
      heapify(inputData, i - 1, inputData.length - 1)
      i = i - 1
    }
    var j = inputData.length - 1
    while (j > 0) {
      val x = inputData(0)
      inputData(0) = inputData(j)
      inputData(j) = x
      heapify(inputData, 0, j - 1)
      j = j - 1 }
    inputData
  }

}


