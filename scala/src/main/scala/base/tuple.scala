package base

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/9/28
  * \* Time: 10:44
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object tuple {
  def main(args: Array[String]): Unit = {
    //1
    val t1:(Int,String)=(1,"2")
    val t2:Tuple2[Int,String]=(1,"2")
    val (t11,t22,t3)=("world","!",0x22)
    val (t4,t5,t6)=Tuple3("world","!",0x22)//调用Tuple工厂方法apply
    println(t11+","+t22+","+t3)
    //二元组称为pair,定义pair的方法为
    (1,"one")
    1->"one"
    Tuple2(1,"one")
  }
}

