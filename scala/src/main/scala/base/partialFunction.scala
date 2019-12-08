package base

/**
  * Created by XP on 2017/9/27.
  */
object partialFunction {
  //不处理所有可能输入，只处理能与case符合的输入
  def main(args: Array[String]): Unit = {
    val pf1:PartialFunction[Any,String]={case s:String =>"yes"}
    val pf2:PartialFunction[Any,String]={case s:Double =>"yes"}
    val pf=pf1 orElse pf2//链式链接，pf1不符合则跳转到下一个函数
    def tryPF(x:Any,f:PartialFunction[Any,String]):String=
      try{f(x).toString} catch {case _:MatchError =>"error"}
    def d(x:Any,f:PartialFunction[Any,String])=
      f.isDefinedAt(x).toString//判断是否与偏函数匹配，避免matcherror
//    List("str",3.14,10).foreach{
//      x=>printf("%-5s | % -5s |% -6s |% -5s |% -6s |% -5s |% -6s\n",
//        x.toString,d(x,pf1),tryPF(x,pf1),d(x,pf2),tryPF(x,pf2),d(x,pf),tryPF(x,pf))
//    }

    List("str",3.14,10).foreach{x=>printf(d(x,pf1))}

  }
}
