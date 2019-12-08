import java.math.BigInteger

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/26
  * \* Time: 14:06
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object test {
  def main(args: Array[String]): Unit = {
    val a ="12022000"
   val  b = new BigInteger(a)
   val c =b.remainder(new BigInteger("100"))
    println(c)
    println(c.getClass)
    println(c.intValue())
    println(c.intValue().getClass)

  }
}

