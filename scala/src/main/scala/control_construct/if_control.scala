package control_construct

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/12
  * \* Time: 17:07
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object if_control {
  def main(args: Array[String]): Unit = {
    //scala中的if可有带有返回值
    val configFile =new java.io.File("/data")
    val configPath=if(configFile.exists()){
      configFile.getAbsolutePath
    }else{
      configFile.createNewFile()
      configFile.getAbsolutePath
    }
    println(configPath)
  }
}

