package base

import scala.language.reflectiveCalls

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/12
  * \* Time: 18:14
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object ttt {
  def main(args: Array[String]): Unit = {

  }

}
object manage{
//  def apply[R <: {def close():Unit},//R表示要管理的资源类型，<:表示R属于某其他类型的子类，此处R属于{def close():Unit}方法的子类
//  T]//传入了用于处理资源的匿名函数，T表示该匿名函数的返回值
//  (resource =>R)//resource时一个传名参数，可以暂时看做时一个调用时应省略括号的函数
//  (f:R=>T) //第二个参数列表，包含一个输入为resource、返回类型为T的匿名函数，该匿名函数将负责处理resource
//    ={
//    val res:Option[R]=None
//    try {
//      res =Some(resource)
//      f(res.get)
//    }catch {
//      case NonFatal(ex)=>println( s"non fatal exception !$ex")
//    }finally {
//      if (res!=None){
//        println(s"closing resource ...")
//        res.get.close
//      }
//    }
//  }
}

