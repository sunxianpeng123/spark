package functional_programming

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/18
  * \* Time: 15:08
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object ListSeqVector_FP {
  def main(args: Array[String]): Unit = {
    /**
      * list
      */
    //1
    val list1=List("programming","scala")
    val list2="people"::"should"::"read"::list1
    println(list1)
    println(list2)
//2、Nil空队列
    val list3="programma"::"scala"::Nil
    val l=List.empty[String]
    println(l)
    //
    println(list1++list2)
    println("======================")
    /**
      * seq
      * seq只是一个特征，并不是具体的类
      * 使用Seq.apply时，会创建出一个List
      */
    val seq1=Seq("programming","scala")
    val seq2="people"+:"should"+:"read"+:seq1
    val seq3="people"+:"should"+:"read"+:Seq.empty
    val seq4=seq1++seq2
    println(seq1)
    println(seq2)
    println(seq3)
    println(seq4)
    println("==================")
    /**
      * vector,可以用来代替list，其所有操作均为o(1)，而list为o（n）
      *
      */
    val vector1=Vector("programming","scala")
    val vector2="people"+:"should"+:"read"+:vector1
    val vector3="people"+:"should"+:"read"+:Vector.empty
    val vector4=vector1++vector2
    println(vector1)
    println(vector2)
    println(vector3)
    println(vector4)
    println(vector4(1))//时间复杂度为常数


  }
}

