package com.fueledbycaffeine.bunnypedia.ext.android

import android.os.Build
import android.text.Html
import android.widget.TextView
import com.fueledbycaffeine.bunnypedia.util.ListTagHandler

fun TextView.setHtmlText(html: String) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
    text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
  } else {
    @Suppress("DEPRECATION")
    text = Html.fromHtml(html, null, ListTagHandler())
  }
}
