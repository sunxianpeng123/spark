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
object SinsrtSort {
  def main(args: Array[String]): Unit = {
    val testData = new ArrayBuffer[Int]
    testData += 10;testData += 101;testData += 99;testData += 3;testData += 9;testData += 12;testData += 77;testData += 86;testData += 99;testData += 25
    println("testData = " + testData)
    val sinsrtSortData = SinsrtSort(testData)
    println("sinsrtSortData = " + sinsrtSortData)
    val mySinsrtSortData = mySinsrtSort(testData)
    println("mySinsrtSortData = " + mySinsrtSortData)
  }
  /*函数功能：直接插入排序*/
  /**
    * 将数据分为有序数列和无序数列两部分，一开始有序数列只有数列的第一个元素。对于无序数列的每个元素，要插入到有序数列中，
    * 方法是与有序数列中的末尾元素开始作比较，若前者大于后者则前者位置不变；否则，检索这个待插入元素小于第n个元素且大于或等于第n-1个元素的位置，
    * 同时将这个位置后面的元素往后移动(检索一次，移动一次)，然后将其插入到这个位置。以此类推，直到无序数列元素个数为0。
    *
    * @param inputData
    * @return
    */
  def SinsrtSort(inputData: ArrayBuffer[Int]): ArrayBuffer[Int] = {
    for (i <- 1.until(inputData.length)) {
      var j = i - 1
      val temp = inputData(i)
      while (j >= 0 && temp < inputData(j)) {
        inputData(j + 1) = inputData(j)
        j -= 1
      }
      inputData(j + 1) = temp
    }
    inputData
  }
  def mySinsrtSort(inputData: ArrayBuffer[Int]): ArrayBuffer[Int] = {
    for (i <- 1.until(inputData.length)) {
      val temp = inputData(i)
      var j = i - 1
      while (j >= 0 && temp < inputData(j)) {
        inputData(j + 1) = inputData(j)
        j -= 1
      }
      inputData(j + 1) = temp
    }
    inputData
  }
}


