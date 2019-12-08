package bean

import com.google.gson.annotations.SerializedName
import org.codehaus.jackson.annotate.JsonIgnoreProperties

/**
  * Created by xiangjia on 2016/12/28 0028.
  */
@JsonIgnoreProperties(ignoreUnknown = true)
class ItemBean extends Serializable  {
   @SerializedName("type")
   var typeName = ""
   var time=""
   var app=""
   var fromid=""
   var fromname=""
   var roomgroup=""
   var platgroup=""
   var content=""
   var price=""
   var count=0
}
