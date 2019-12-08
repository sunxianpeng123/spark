package base

/**
  * Created by XP on 2017/9/27.
  */
object method_stat {

    abstract class Shape(){
      def draw(offset:Point=Point(0.0,0.0))(f:String =>Unit):Unit={
        f(s"draw(offset =$offset),${this.toString}")
      }
    }
  case class Point(x:Double=0.0,y:Double=0.0)extends Shape{
    def shift(delatx:Double,delaty:Double)={
      copy(x+delatx,y+delaty)//
    }
    case class Circle(center:Point,radius:Double)extends Shape
    case  class Rectangle(lowerLeft:Point,height:Double,width:Double) extends Shape

  def main(args: Array[String]): Unit = {
    //1

      val p1=new Point(x=3.3,y=4.4)
      val p2=p1.copy(y=6.6)//创建实例时，值给出与原对象不同的参数
      //2 multi can shu  lie biao
      p1.draw(Point(1.0,2.0))(str=>printf(s"Shape draw actor:$str"))

    //3 m1 不能推断函数参数
    def m1[A](a:A,f:A=>String)=f(a)
    def m2[A](a:A)(f:A=>String)=f(a)
    //printf(    m1(100,i=>s"$i+$i"))
    printf(  m2(100)(i=>s"$i+$i"))

    }
  }
}
