package bean

import org.codehaus.jackson.annotate.JsonIgnoreProperties

/**
  * Created by xiangjia on 2016/12/28 0028.
  */
@JsonIgnoreProperties(ignoreUnknown = true)
class MsgBean extends Serializable  {
   var time=""
   var content=""
}
