package bean

import org.codehaus.jackson.annotate.JsonIgnoreProperties

/**
  * Created by xiangjia on 2016/12/28 0028.
  */
@JsonIgnoreProperties(ignoreUnknown = true)
class rankScheduleBean extends Serializable  {
   var id  = 0
   var key_word=""
   var weight_proportion=""



}
