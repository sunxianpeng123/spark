name := "spark_test"

version := "1.0"

scalaVersion := "2.10.6"
//spark
libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.6.0"  % "provided"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.3"% "provided"
libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "2.1.0"
libraryDependencies += "org.apache.spark" % "spark-sql_2.10" % "2.1.0"
libraryDependencies += "org.apache.spark" % "spark-hive_2.10" % "2.1.0" % "provided"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "2.1.0"// % "provided"
////
libraryDependencies += "org.apache.commons" % "commons-pool2" % "2.4.2"
libraryDependencies += "org.apache.hive" % "hive-jdbc" % "1.1.0"//%"provided"
//分词
libraryDependencies += "org.apdplat" % "word" % "1.2"%"provided"
libraryDependencies += "org.ansj" % "ansj_seg" % "5.1.0"
libraryDependencies += "com.huaban" % "jieba-analysis" % "1.0.2"
//
libraryDependencies += "log4j" % "log4j" % "1.2.16"//% "provided"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.33"
libraryDependencies += "org.codehaus.jackson" % "jackson-core-asl" % "1.9.13"
