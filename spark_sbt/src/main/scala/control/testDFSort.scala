package control

import org.apache.spark.sql.DataFrame

/**
  * Created by Dell on 2017/6/26.
  */
class testDFSort {
  def test(sqlDate: DataFrame): Unit = {
    sqlDate.sort("room_id.asc").show()

  }
}
