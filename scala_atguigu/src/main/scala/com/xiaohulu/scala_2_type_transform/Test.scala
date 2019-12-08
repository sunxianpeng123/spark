package com.xiaohulu.scala_2_type_transform

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/21
  * \* Time: 20:11
  * \* To change this template use File | Settings | File Templates.
  * \* Description:
  * \*/
object Test {
  def main(args: Array[String]): Unit = {
    val pf:PartialFunction[Int,String] = {
      case 1=>"One"
      case 2=>"Two"
      case 3=>"Three"
      case _=>"Other"
    }
    println(pf.isDefinedAt(1))

    val onePF:PartialFunction[Int,String] = {
      case 1=>"One"
    }


    val twoPF:PartialFunction[Int,String] = {
      case 2=>"Two"
    }


    val threePF:PartialFunction[Int,String] = {
      case 3=>"Three"
    }


    val otherPF:PartialFunction[Int,String] = {
      case _=>"Other"
    }

    val newPF = onePF orElse twoPF orElse threePF orElse otherPF
    println(newPF)

    val pf1:PartialFunction[Int,String] = {
       case i if i == 1 => "One"
      }
    val pf2:PartialFunction[String,String] = {
        case str if str eq "One" => "The num is 1"
      }
    val num = pf1 andThen pf2
    println(num)

    val apporelse= onePF.applyOrElse(1,{num:Int=>"two"})
    println(apporelse)

  }
}
