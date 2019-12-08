
/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/6/3
  * \* Time: 18:43
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
// 私有构造方法
class AssociatedObject private(val color:String) {

  println("创建" + this)

  override def toString(): String = "颜色标记："+ color

}


// 伴生对象，与类名字相同，可以访问类的私有属性和方法
object AssociatedObject {
  private val markers: Map[String, AssociatedObject] = Map(
    "red" -> new AssociatedObject("red"),
    "blue" -> new AssociatedObject("blue"),
    "green" -> new AssociatedObject("green")
  )

  def apply(color:String) = {
    if(markers.contains(color)) markers(color) else null
  }

  def getMarker(color:String) = {
    if(markers.contains(color)) markers(color) else null
  }

  def main(args: Array[String]) {
    println(AssociatedObject("blue"))//1、先执行private val markers，2、
    // 单例函数调用，省略了.(点)符号
//    println(AssociatedObject getMarker "blue")
  }
}

