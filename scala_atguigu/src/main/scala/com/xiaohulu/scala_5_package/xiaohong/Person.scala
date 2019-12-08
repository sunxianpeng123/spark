package com.xiaohulu.scala_5_package.xiaohong

import scala.beans.BeanProperty

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2019/8/23
  * \* Time: 19:40
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
class Person {
  //第一种形式
  @BeanProperty var name: String = _
  //第二种形式, 和第一种一样，都是相对路径引入
  @scala.beans.BeanProperty var age: Int = _
  //第三种形式, 是绝对路径引入，可以解决包名冲突
  @_root_. scala.beans.BeanProperty var sex: String = _
}
