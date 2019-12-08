package base

/**
  * Created by XP on 2017/9/27.
  */
object range {
  def main(args: Array[String]): Unit = {
    println(1 to 10)//含10
    println( 1 until 10 )//不含10
    println(1 to 10 by 3)
    println(10 to 1 by -3)
    println(1L to 10L by 3)
    println(1.1f to 10.3f by 3.1f)
    println(1.1f to 10.3f by 0.5f)
    println(1.1 to 10.3 by 3.1)//double
    println('a' to 'g' by 3)
    println(BigInt(1) to BigInt(10) by 3)
    println(BigDecimal(1.1) to BigDecimal(10.3) by 3.1)
    println("("+(1 until 10  mkString(","))+")")

  }
}
