package base

import jdk.nashorn.internal.ir.LiteralNode.PrimitiveLiteralNode

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/9/28
  * \* Time: 11:04
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object OptionSomeNone {
  //some 和 none 是Option的子类，some表示有值，none表示无值
  def main(args: Array[String]): Unit = {
    val statMap=Map(
      "A"->"a",
      "B"->"b",
      "C"->"c"
    )
    //1 map的get方法返回option[T],即some（val）或者None，分别表示有值和无值，类似java语言map的get方法在存在数返回值，不存在时返回null
    println("//1 map的get方法返回option[T],即some（val）或者None，分别表示有值和无值，类似java语言map的get方法在存在数返回值，不存在时返回null=======")
    println(statMap.get("A"))//
    println(statMap.get("Unknow"))
    //2
    println("//2====================")
    println(statMap.get("A").get)//get方法存在时返回值不存在时报错
    println(statMap.getOrElse("B","no val"))
    println(statMap.get("B").getOrElse("no val"))//getorelse存在时返回值不存在时返回设置的元素
    println(statMap.get("unknow").getOrElse("no val"))
    println("//3过滤option[None]============")
    val res:Seq[Option[Int]]=Vector(Some(10),None,Some(20))
    val resC=res.withFilter{
      case Some(i)=>true
      case None=>false
    }.map{
      case  Some(i)=>(2*i)
    }.foreach(println)
    println("//4   Option:::Either类型的逻辑扩展====================")
    def positive(i:Int):Either[String,Int]=
      if (i>0) Right(i) else  Left(s"nonpositive number $i")
    val a =for{
      i1 <-positive(5).right
      i2 <-positive(-1*i1).right
      i3 <-positive(25*i2).right
      i4 <-positive(2*i3).right
    } yield i1+i2+i3+i4
    println(a)
    println("//4.1  right ,left======================")
    val l :Either[String,Int]=Left("boo")
    val r :Either[String,Int]=Right(12)
    println(l.left)
    println(l.right)
    println(r.left)
    println(r.right)

  }


}

