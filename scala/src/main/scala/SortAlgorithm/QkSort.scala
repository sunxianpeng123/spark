package SortAlgorithm

import java.io.PrintWriter

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Random

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/9/25
  * \* Time: 10:25
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object QkSort {
  def main(args: Array[String]): Unit = {
    //    val testData = new ArrayBuffer[Int]
    //    testData += 10;testData += 101;testData += 75;testData += 3;testData += 9;testData += 12;testData += 77;testData += 86;testData += 99;testData += 25
    val testData = ArrayBuffer(10, 101, 75, 3, 9, 12, 77, 86, 99, 25)
    println("testData = "+testData)
    val qkSortData=qksort(testData)
    println("qkSortData = "+qkSortData)
    val myQkSortData=qksort(testData)
    println("myQkSortData = "+myQkSortData)

  }
  /*函数功能：快速排序*/
  def qksort(inputData: ArrayBuffer[Int]): ArrayBuffer[Int] = {
    def qs(inputData:ArrayBuffer[Int],left:Int,right:Int):Unit={
      if(left<right) {
        var i = left
        var j = right
        val x = inputData(i)
        while (i < j) {
          while (i < j && inputData(j) > x) j = j - 1 /* 从右向左找第一个小于x的数 */
          if (i < j) {
            inputData(i) = inputData(j)
            i = i + 1
          }
          while (i < j && inputData(i) < x) i = i + 1 /* 从左向右找第一个大于x的数 */
          if (i < j) {
            inputData(j) = inputData(i)
            j = j - 1
          }
        }
        inputData(i)=x
        qs(inputData,left,i-1)
        qs(inputData,i+1,right)
      }
    }
    qs(inputData,0,inputData.length-1)
    inputData
  }
  def myqkSort(inputData:ArrayBuffer[Int]):ArrayBuffer[Int]={
    def quick_sort(inputData:ArrayBuffer[Int],left:Int,right:Int):ArrayBuffer[Int]={
      if(left<right){
        var i =left
        var j =right
        var x =inputData(i)
        while(i<j){
          while(i < j && inputData(j)>x) j-=1
          if(i<j){
            inputData(i)=inputData(j)
            i+=1
          }
          while(i < j && inputData(i) < x) j-=1
          if(i<j){
            inputData(j)=inputData(i)
            j-=1
          }
        }
        quick_sort(inputData,left,i-1)
        quick_sort(inputData,i+1,right)
      }
      inputData
    }
    quick_sort(inputData,0,inputData.length-1)
    inputData
  }
}


