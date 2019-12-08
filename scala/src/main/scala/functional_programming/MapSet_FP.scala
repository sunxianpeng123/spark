package functional_programming

import scala.io.Source

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/18
  * \* Time: 16:09
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object MapSet_FP {
  def main(args: Array[String]): Unit = {
    /**
      * Map
      */
    val stateCaptial=Map(
      "a"->"123",
      "b"->"456"
    )
    val lengths=stateCaptial map(kv=>{
      (kv._1,kv._2.length)
    })
    val stateCaptial2=stateCaptial +(
      "c"->"789",
      "d"->"101112"
    )
    println(stateCaptial)
    println(lengths)
    println(stateCaptial2)
    val stateCaptial3=stateCaptial + "c"->"789"
    println(stateCaptial3)
    println("==========================")
    /**
      * set
      */
    val states_set=Set("a","b","c")
    val lengths_set=states_set.map(st =>{st.length})
    val states2_set=states_set +"d"
    val states3_set=states2_set+("e","f")
    println(states_set)
    println(lengths_set)
    println(states2_set)
    println(states3_set)

  }
}

