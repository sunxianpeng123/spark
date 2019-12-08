package com.xiaohulu.source

import org.codehaus.jackson.annotate.JsonIgnoreProperties

/**
  * Created by xiangjia on 2016/12/28 0028.
  */
@JsonIgnoreProperties(ignoreUnknown = true)
class DateBean extends Serializable {
  var item:Array[ItemBean] = Array.empty
  var sid = ""
  var roomid = ""
  var sourcelink=""
}
