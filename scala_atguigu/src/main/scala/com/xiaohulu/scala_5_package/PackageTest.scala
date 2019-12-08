/**
  * 1、com.xiaohulu 表示创建了包com.xiaohulu，在{}中，
  * 我们可以继续写他的子包 scala_5_package//com.xiaohulu.scala_5_package,
  * 还可以写特质trait ，object
  * 2、即scala支持在一个文件中，可以同时创建多个包，以及给多个包创建类，trait，object
  * */
package   com.xiaohulu{//包com.xiaohulu
  package scala_5_package{

    import com.xiaohulu.testpackage.{Animal, Machine}

    //包com.xiaohulu.scala_5_package
    object Test{
    def main(args: Array[String]): Unit = {
       val animal = new Animal
      val machine = new Machine
      println(s"animal = ${animal.name},machine = ${machine.name} ")
    }
  }
  }
  package testpackage{//包com.xiaohulu.testpackage
    class Animal{//表示在包com.xiaohulu.testpackage 下创建类Animal
      val name ="cat"
    }
    class Machine{//表示在包com.xiaohulu.testpackage 下创建类Machine
    val name = "Jeep"
    }
  }


}
