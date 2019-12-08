import breeze.linalg.DenseVector

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/1/30
  * \* Time: 11:24
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object testssss {
  def main(args: Array[String]): Unit = {
    val row="(6,20641,105781374,DenseVector(0.0, 0.0),DenseVector(0.0, 0.0),41)"
    val s1 =row.replaceAll("\\(","")//(6,20641,105781374,DenseVector(0.0, 0.0),DenseVector(0.0, 0.0),41)
    val s2 =s1.split("DenseVector")//6,20641,105781374,DenseVector0.0, 0.0),DenseVector0.0, 0.0),41)
    val s21 =s2(0)//6,20641,105781374,
    val s22=s2(1)//0.0, 0.0)
    val s23=s2(2)//0.0, 0.0),41)
    val plat=s21.split(",")(0).toInt
    val room=s21.split(",")(1)
    val from =s21.split(",")(2)
    val labelV=s22.split("\\)")(0).split(",")//0.0, 0.0
    val labelVec:DenseVector[Double] =DenseVector.zeros(labelV.length)
    (0 until labelV.length ).foreach(index=>{
      labelVec(index)=labelV(index).toDouble
    })
    val originV=s23.split("\\)")(0).split(",")//0.0, 0.0
    val originalVec:DenseVector[Double]=DenseVector.zeros(originV.length)
    (0 until originV.length ).foreach(index=>{
      originalVec(index)=originV(index).toDouble
    })
    val group=s23.split("\\)")(1).replaceAll(",","").toInt//41
    println(group)


  }
}

