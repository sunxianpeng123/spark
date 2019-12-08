package sparkSQL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/18
  * \* Time: 14:25
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object DataSourceParquet {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder().appName("test").config("spark.some.config.option","some value").master("local[*]").getOrCreate()
    println("以编程的方式加载数据============================")
    import spark.implicits._
    val peopleDF=spark.read.json("F:\\SoftWare\\spark-2.1.0-bin-hadoop2.7\\spark-2.1.0-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")
    peopleDF.write.parquet("people.parquet")
    val parquetFileDF = spark.read.parquet("people.parquet")
    parquetFileDF.createOrReplaceTempView("parquetFile")
    val namesDF = spark.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19")
    namesDF.map(attributes => "Name: " + attributes(0)).show()



    println("分区发现=======================================")
//    在系统中，比如 Hive，表分区是一个很常见的优化途径。在一个分区表中 ，数据通常存储在不同的文件目录中，
//    对每一个分区目录中的途径按照分区列的值进行编码。Parquet 数据源现在可以自动地发现并且推断出分区的信息。
//    例如，我们可以将之前使用的人口数据存储成下列目录结构的分区表，两个额外的列，gender 和 country 作为分区列 :
    //    path
//    └── to
//      └── table
//      ├── gender=male
//    │   ├── ...
//    │   │
//      │   ├── country=US
//    │   │   └── data.parquet
//    │   ├── country=CN
//    │   │   └── data.parquet
//    │   └── ...
//    └── gender=female
//    ├── ...
//    │
//    ├── country=US
//    │   └── data.parquet
//    ├── country=CN
//    │   └── data.parquet
//    └── ...
//    通过向 SparkSession.read.parquet 或 SparkSession.read.load 中传入 path/to/table，Spark SQL 将会自动地从路径中提取分区信息。
    // 现在返回的 DataFrame 模式变成 :
//      root
//    |-- name: string (nullable = true)
//    |-- age: long (nullable = true)
//    |-- gender: string (nullable = true)
//    |-- country: string (nullable = true)
//    需要注意的是分区列的数据类型是自动推导出的。当前，支持数值数据类型以及 string 类型。有些时候用户可能不希望自动推导出分区列的数据类型。
//     对于这些使用场景，自动类型推导功能可以通过 spark.sql.sources.partitionColumnTypeInference.enabled 来配置，
//     默认值是 true。当自动类型推导功能禁止，分区列的数据类型是 string。
//     从 Spark 1.6.0 开始，分区发现只能发现在默认给定的路径下的分区。
//     对于上面那个例子，如果用户向 SparkSession.read.parquet 或 SparkSession.read.load, gender 传递 path/to/table/gender=male
//     将不会被当做分区列。如果用户需要指定发现的根目录，可以在数据源设置 basePath 选项。比如，将 path/to/table/gender=male
//     作为数据的路径并且设置 basePath 为 path/to/table/，gender 将会作为一个分区列。
    println("Schema 合并================================")
//    类似 ProtocolBuffer，Avro，以及 Thrift，Parquet 也支持 schema 演变。用户可以从一个简单的 schema 开始，
//    并且根据需要逐渐地向 schema 中添加更多的列。这样，用户最终可能会有多个不同但是具有相互兼容 schema 的 Parquet 文件。
//    Parquet 数据源现在可以自动地发现这种情况，并且将所有这些文件的 schema 进行合并。 由于 schema 合并是一个性格
//    开销比较高的操作，并且在大部分场景下不是必须的，从 Spark 1.5.0 开始默认关闭了这项功能。 你可以通过以下方式开启 :
//    设置数据源选项 mergeSchema 为 true 当读取 Parquet 文件时（如下面展示的例子），或者
//    这是全局 SQL 选项 spark.sql.parquet.mergeSchema 为 true。
    import spark.implicits._

    // Create a simple DataFrame, store into a partition directory
    val squaresDF = spark.sparkContext.makeRDD(1 to 5).map(i => (i, i * i)).toDF("value", "square")
    squaresDF.write.parquet("data/test_table/key=1")

    // Create another DataFrame in a new partition directory,
    // adding a new column and dropping an existing column
    val cubesDF = spark.sparkContext.makeRDD(6 to 10).map(i => (i, i * i * i)).toDF("value", "cube")
    cubesDF.write.parquet("data/test_table/key=2")

    // Read the partitioned table
    val mergedDF = spark.read.option("mergeSchema", "true").parquet("data/test_table")//合并起来，若value值相同，则组成一行，否则添加额外一行
    mergedDF.show()
    mergedDF.printSchema()
    println("Hive metastore Parquet 表转换=======================")
//    当从 Hive metastore 里读写 Parquet 表时，为了更好地提升新能 Spark SQL 会尝试用自己支持的 Parquet 替代 Hive SerDe。
//    这个功能通过 spark.sql.hive.convertMetastoreParquet 选项来控制，默认是开启的。
    println("Hive/Parquet Schema Reconciliation*************************")
//    从 Hive 和 Parquet 处理表 schema 过程的角度来看有两处关键的不同。
//    Hive 对大小写不敏感，而 Parquet 不是。
//    Hive 认为所有列都是 nullable 可为空的，在 Parquet 中为 nullability 是需要显示声明的。
//    由于这些原因，当我们将 Hive metastore Parquet table 转换为Spark SQLtable 时必须使 Hive metastore schema 与 Parquet schema 相兼容。兼容规则如下 :
//      除了nullability，相同 schema 的字段的数据类型必须相同。要兼容的字段应该具有 Parquet 的数据类型，因此 nullability 是被推崇的。
//    reconciled schema 包含了这些 Hive metastore schema 里定义的字段。
//    任何字段只出现在 Parquet schema 中会被 reconciled schema 排除。
//    任何字段只出现在 Hive metastore schema 中会被当做 nullable 字段来添加到 reconciled schema 中。
    println("Metadata 刷新****************")
//    为了提高性能 Spark SQL 缓存了 Parquet metadata。当 Hive metastore Parquet table 转换功能开启，这些转换后的元数据信息也会被缓存。
//    如果这些表被 Hive 或者其他外部的工具更新，你需要手动刷新以确保元数据信息保持一致。
    spark.catalog.refreshTable("my_table")
    println("Parquet 配置===================================")
//    Parquet 的配置可以使用 SparkSession 的 setConf 来设置或者通过使用 SQL 运行 SET key=value 命令。
//    具体查阅官方文档











    //    println("Parquets文件格式==========================")
//    println("读取Parquet文件（Loading Data Programmatically）")
//    val parquetFileDF=spark.read.parquet("namesAndAges.parquet")
//    parquetFileDF.createOrReplaceTempView("parquetFile")
//    val namesDF=spark.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19")
//    //namesDF.map(attributes => "Name: " + attributes(0)).show()
//    namesDF.show()



  }
}

