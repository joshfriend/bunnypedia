package com.fueledbycaffeine.bunnypedia.util

import android.text.Editable
import android.text.Html.TagHandler
import org.xml.sax.XMLReader


class ListTagHandler: TagHandler {
  private var first = true
  private var parent: String? = null
  private var index = 1

  override fun handleTag(
    opening: Boolean,
    tag: String,
    output: Editable,
    xmlReader: XMLReader
  ) {

    if (tag == "ul") {
      parent = "ul"
    } else if (tag == "ol") {
      parent = "ol"
    }

    if (tag == "li") {
      if (parent == "ul") {
        if (first) {
          output.append("\n• ")
          first = false
        } else {
          first = true
        }
      } else {
        if (first) {
          output.append("\n$index. ")
          first = false
          index++
        } else {
          first = true
        }
      }
    }
  }
}
