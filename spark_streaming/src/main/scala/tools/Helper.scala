package tools

import java.text.SimpleDateFormat
import java.util.Date


/**
  * Created by xiangjia on 2017/1/6 0006.
  */
object Helper {
  def filterEmoji(source: String): String = {
    if (source != null && source.length() > 0) {
      source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "")
    } else {
      source
    }

  }

  def getNowDate():String={
    val now:Date = new Date()
    val  dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS")
    val hehe = dateFormat.format( now )
    hehe
  }


  def getNowDateFormat2():String={
    val now:Date = new Date()
    val  dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm")
    val hehe = dateFormat.format( now )
    hehe
  }
}
