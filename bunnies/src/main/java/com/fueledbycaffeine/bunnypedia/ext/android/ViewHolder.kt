@file:Suppress("NOTHING_TO_INLINE")

package com.fueledbycaffeine.bunnypedia.ext.android

import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

inline fun RecyclerView.ViewHolder.getString(@StringRes res: Int, vararg args: String): String {
  return itemView.context.getString(res, args)
}
