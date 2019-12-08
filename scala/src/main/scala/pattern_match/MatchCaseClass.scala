package pattern_match

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/19
  * \* Time: 17:41
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object MatchCaseClass {
  def main(args: Array[String]): Unit = {
    case class Address(street:String,city:String,country:String)
    case class Person(name:String,age:Int,address: Address)

    val alice=Person("Alice",25,Address("1 scala lane","chicage","usa"))
    val bob=Person("bob",29,Address("2 java ave","miami","usa"))
    val charlie=Person("charlie",32,Address("3 python ct","boston","usa"))

    for (person <- Seq(alice, bob, charlie)) {
      person match {
        case Person("Alice",25,Address(_,"chicage",_)) => println("hello alice")
        case Person("bob",29,Address("2 java ave","miami","usa")) =>println("hi bob")
        case Person(name,age,_)=> println(s"who are you ，$age year-old person named $name")
      }
    }
    println("case的变量绑定")
    for (person <- Seq(alice, bob, charlie)) {
      person match {
        case p @ Person("Alice",25,Address(_,"chicage",_)) => println(s"hello alice $p")
        case p @ Person("bob",29,Address("2 java ave","miami","usa")) =>println(s"hi bob $p")
        case p @ Person(name,age,_)=> println(s"who are you ，$age year-old person named $name ? $p")
      }
    }

  }


}

