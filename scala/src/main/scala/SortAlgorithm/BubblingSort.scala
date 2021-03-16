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
object BubblingSort {
  /**
    * 比较相邻元素的大小，对于每次循环，按排序的规则把最值移向数组的一端，同时循环次数依次减少
    * @param args
    */
  def main(args: Array[String]): Unit = {
//    val testData = new ArrayBuffer[Int]
//    testData += 10;testData += 101;testData += 75;testData += 3;testData += 9;testData += 12;testData += 77;testData += 86;testData += 99;testData += 25
    val testData = ArrayBuffer(10, 101, 75, 3, 9, 12, 77, 86, 99, 25)
    println("testData = "+testData)
    val bubblingSortData=bubblingSort(testData)
    println("bubblingSortData = "+bubblingSortData)
    val myBubblingSortData=muBubblingSort(testData)
    println("myBubblingSortData = "+myBubblingSortData)
  }
  /*函数功能：冒泡排序*/
  def bubblingSort(inputData: ArrayBuffer[Int]): ArrayBuffer[Int] = {
    for (j <- 0 to inputData.length - 3)
      for (i <- 0 to inputData.length - 2 - j) {
        if (inputData(i) > inputData(i + 1)) {
          //判断前一个元素是否大于后一个，如果大于，则交换
          val temp = inputData(i + 1)
          inputData(i + 1) = inputData(i)
          inputData(i) = temp
        }
      }
    inputData
  }
  def muBubblingSort(inputData :ArrayBuffer[Int]):ArrayBuffer[Int]={
    for (j<-1.until(inputData.length-1)){
      for (i <- 0.until(j)){
        if(inputData(i)>inputData(j)){
          val temp =inputData(i)
          inputData(i)=inputData(j)
          inputData(j)=temp
        }
      }
      inputData
    }
    inputData
  }
}


