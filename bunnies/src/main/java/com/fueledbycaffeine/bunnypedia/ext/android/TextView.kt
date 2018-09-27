package com.fueledbycaffeine.bunnypedia.ext.android

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import com.fueledbycaffeine.bunnypedia.util.ListTagHandler

fun TextView.setHtmlText(html: String) {
  text = html.spannableHtml()
}

fun String.spannableHtml(): Spanned {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
  } else {
    @Suppress("DEPRECATION")
    Html.fromHtml(this, null, ListTagHandler())
  }
}

fun String.stripHtmlTags(): String = this.spannableHtml().toString()
