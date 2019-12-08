//package sparkSQL
//
//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.sql.{Row, SparkSession}
//
///**
//  * Created by Dell on 2017/7/10.
//  */
//object DataSourceHive {
//  /**
//    * Spark SQL 可以自动的推断出 JSON 数据集的 schema 并且将它作为 DataFrame 进行加载。
//    * 这个转换可以通过使用 SparkSession.read.json() 在字符串类型的 RDD 中或者 JSON 文件。
//    * 注意作为 json file 提供的文件不是一个典型的 JSON 文件。每一行必须包含一个分开的独立的有效 JSON 对象。
//    * 因此，常规的多行 JSON 文件通常会失败。
//    * @param args
//    */
//
//  def main(args: Array[String]): Unit = {
//    case class Record(key: Int, value: String)
//    Logger.getLogger("org").setLevel(Level.ERROR)
//
//    // warehouseLocation points to the default location for managed databases and tables
//    val warehouseLocation = "file:${system:user.dir}/spark-warehouse"
//    val spark = SparkSession
//      .builder()
//      .appName("Spark Hive Example")
////      .config("spark.sql.warehouse.dir", warehouseLocation)//192.168.120.160
////      .config("spark.sql.warehouse.dir", "192.168.120.160")//192.168.120.160
//      .enableHiveSupport()
//      .master("local[*]")
//      .getOrCreate()
//
//    import spark.implicits._
//    import spark.sql
////    sql("use live_show_message")
//    println("Hive 表==========================================")
////    Spark SQL 还支持在 Apache Hive 中读写数据。然而，由于 Hive 依赖项太多，这些依赖没有包含在默认的 Spark 发行版本中。
////    如果在 classpath 上配置了 Hive 依赖，那么 Spark 会自动加载它们。注意，Hive 依赖也必须放到所有的 worker 节点上，
////    因为如果要访问 Hive 中的数据它们需要访问 Hive 序列化和反序列化库（SerDes)。
////    Hive 配置是通过将 hive-site.xml，core-site.xml（用于安全配置）以及 hdfs-site.xml（用于HDFS 配置）文件放置在conf/目录下来完成的。
////    如果要使用 Hive, 你必须要实例化一个支持 Hive 的 SparkSession，包括连接到一个持久化的 Hive metastore,
////    支持 Hive 序列化反序列化库以及 Hive 用户自定义函数。即使用户没有安装部署 Hive 也仍然可以启用 Hive 支持。
////    如果没有在 hive-site.xml 文件中配置，Spark 应用程序启动之后，上下文会自动在当前目录下创建一个 metastore_db
////      目录并创建一个由 spark.sql.warehouse.dir 配置的、默认值是当前目录下的 spark-warehouse 目录的目录。
////    请注意 : 从 Spark 2.0.0 版本开始，hive-site.xml 中的 hive.metastore.warehouse.dir 属性就已经过时了，你可以使用
////    spark.sql.warehouse.dir 来指定仓库中数据库的默认存储位置。你可能还需要给启动 Spark 应用程序的用户赋予写权限。
//    // Queries are expressed in HiveQL
//    sql("SELECT * FROM src").show()
//    // +---+-------+
//    // |key|  value|
//    // +---+-------+
//    // |238|val_238|
//    // | 86| val_86|
//    // |311|val_311|
//    // ...
//
//
//    // Aggregation queries are also supported.
//    sql("SELECT COUNT(*) FROM src").show()
//    // +--------+
//    // |count(1)|
//    // +--------+
//    // |    500 |
//    // +--------+
//
//
//    // The results of SQL queries are themselves DataFrames and support all normal functions.
//    val sqlDF = sql("SELECT key, value FROM src WHERE key < 10 ORDER BY key")
//
//
//    // The items in DaraFrames are of type Row, which allows you to access each column by ordinal.
//    val stringsDS = sqlDF.map {
//      case Row(key: Int, value: String) => s"Key: $key, Value: $value"
//    }
//    stringsDS.show()
//    // +--------------------+
//    // |               value|
//    // +--------------------+
//    // |Key: 0, Value: val_0|
//    // |Key: 0, Value: val_0|
//    // |Key: 0, Value: val_0|
//    // ...
//
//
//    // You can also use DataFrames to create temporary views within a HiveContext.
//    val recordsDF = spark.createDataFrame((1 to 100).map(i => Record(i, s"val_$i")))
//    recordsDF.createOrReplaceTempView("records")
//
//
//    // Queries can then join DataFrame data with data stored in Hive.
//    sql("SELECT * FROM records r JOIN src s ON r.key = s.key").show()
//    // +---+------+---+------+
//    // |key| value|key| value|
//    // +---+------+---+------+
//    // |  2| val_2|  2| val_2|
//    // |  2| val_2|  2| val_2|
//    // |  4| val_4|  4| val_4|
//    // ...
//  println("与不同版本的 Hive Metastore 交互============================")
//    //详细参考官方文档
//
//  }
//}
