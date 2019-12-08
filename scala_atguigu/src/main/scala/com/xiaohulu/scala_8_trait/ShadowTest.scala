package com.xiaohulu.scala_8_trait

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/26
  * \* Time: 14:23
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
  object ShadowTest {
    def main(args: Array[String]): Unit = {
      val outer1 : ScalaOuterClass = new ScalaOuterClass()
      val outer2 : ScalaOuterClass = new ScalaOuterClass()
      val inner1 = new outer1.ScalaInnerClass()
      val inner2 = new outer2.ScalaInnerClass()
      inner1.test(inner1) // ok, 因为 需要outer1.ScalanInner
  //    默认情况下，scala的内部类实例是和创建该内部类实例的外部类对象关联的（即一一对应的）
       inner1.test(inner2) // error, 需要outer1.ScalanInnerouter2.ScalanInner,使用类型投影后就不会报错
    }
  }
  class ScalaOuterClass {
    myOuter =>
    class ScalaInnerClass { //成员内部类
  //    下面这个方法可以接收又外部类派生出来的任何内部类 ScalaInnerClass 实例
  //    下面的 ScalaOuterClass#ScalaInnerClass 类型投影的作用就是屏蔽外部对象对内部类对象的影响
      def test(ic: ScalaOuterClass#ScalaInnerClass): Unit = {
        System.out.println("使用类型投影 ："+ic)
      }
    }
  }

