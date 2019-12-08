package json



import com.alibaba.fastjson.JSON


/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2018/8/6
  * \* Time: 15:55
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object json {
  def main(args: Array[String]): Unit = {
//    val  json = "[{'学生':'张三', '班级':'一班', '年级':'大一', '科目':'高数', '成绩':90}, {'学生':'李四', '班级':'二班', '年级':'大一', '科目':'高数', '成绩':80}]";
//    val  jsonArray = JSONArray.formatted(json)//(json);
//    val  jsonObject = jsonArray.g.getJSONObject(0);
//    System.out.println(jsonObject.getString("学生"));

    val text = "{\"name\":\"张三\", \"age\":55}"
    val json = JSON.parseObject(text)
    println(json.get("name"))
    println(json.get("age"))

  }
}

