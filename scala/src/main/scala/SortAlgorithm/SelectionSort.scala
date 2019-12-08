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
object SelectionSort {
  def main(args: Array[String]): Unit = {
    val testData = new ArrayBuffer[Int]
    testData += 10;testData += 101;testData += 99;testData += 3;testData += 9;testData += 12;testData += 77;testData += 86;testData += 99;testData += 25
    println("testData = "+testData)
    val selectionSortData=selectionSort(testData)
    println("selectionSortData = "+selectionSortData)
    val mySelectionSortData=mySelectionSort(testData)
    println("mySelectionSortData = "+mySelectionSortData)

  }
  //选择排序
  /**
    * 先在未排序的数组中找出最值，通过交换将其放在数组第一位，然后再从剩余的未排序数组中找到另一个最值，
    * 将其放在已排序数组的末尾（第二次找到该最值后将其放在数组第二位）。以此类推，直到整个数组排完序。
    * @param inputData
    * @return
    */
  def selectionSort(inputData: ArrayBuffer[Int]): ArrayBuffer[Int] = {
    for (i <- 0 until inputData.length - 1) {
      var index = i
      var value = inputData(i)
      for (j <- i + 1 until inputData.length) {
        if (value > inputData(j)) {
          index = j
          value = inputData(j)
        }
      }
      if (index != i) {
        inputData(index) = inputData(i)
        inputData(i) = value
      }
    }
    inputData
  }
  def mySelectionSort(inputData:ArrayBuffer[Int]):ArrayBuffer[Int]={
    for(i <- 0.until(inputData.length-1)){
      var temp =inputData(i)
      for(j<- (i+1).until(inputData.length)){
        if(temp>inputData(j)){
          inputData(i)=inputData(j)
          inputData(j)=temp
          temp=inputData(i)
        }
      }
    }
    inputData
  }
}


