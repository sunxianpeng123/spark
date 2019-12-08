package tools

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
}
