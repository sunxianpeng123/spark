package control_construct

import java.util.Calendar

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/12
  * \* Time: 17:21
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object scalaWhile {
  def main(args: Array[String]): Unit = {

    //do-while
    var count =0
    do{
      count += 1
      println(count)
    }while(count<10)

    //while
    while(!isFridayThriteen(Calendar.getInstance())){
      println("yes")
      Thread.sleep(1000)

    }


  }
  def isFridayThriteen(cal:Calendar):Boolean={
    val dayOfWeek=cal.get(Calendar.DAY_OF_WEEK)
    val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
    (dayOfWeek ==Calendar.FRIDAY)&&(dayOfMonth==13)
  }

}

