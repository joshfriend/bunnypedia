package com.fueledbycaffeine.bunnypedia.ext.android

import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView

fun RecyclerView.ViewHolder.getString(@StringRes res: Int, vararg args: String): String {
  return itemView.context.getString(res, args)
}
