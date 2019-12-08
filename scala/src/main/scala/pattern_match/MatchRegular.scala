package pattern_match

;

/**
  * \* Created with IntelliJ IDEA.
  * \* User: sunxianpeng
  * \* Date: 2017/10/19
  * \* Time: 18:40
  * \* To change this template use File | Settings | File Templates.
  * \* Description: 
  * \*/
object MatchRegular {
  def main(args: Array[String]): Unit = {
    val bookExtractorRE="""book: title=([^,]+),\s+author=(.+)""".r
    val magazineExtractorRE="""magazine: title=([^,]+),\s+issue=(.+)""".r

    val catalog=Seq(
      "book: title=programming scala second edition,author =dear wampler",
      "magazine: title=the new york,issue=january 2014",
      "unknow:title =who put this here??"
    )

    for (item <- catalog) {
      item match {
        case bookExtractorRE(title,author)=> println(s"""book "$title",writen by $author""")
        case magazineExtractorRE(title,issue)=> println(s"""magazine "$title",issue $issue""")
        case entry=>println(s"unrecognized entry:$entry")
      }
    }



  }
}

