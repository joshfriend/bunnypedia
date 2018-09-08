@file:Suppress("NOTHING_TO_INLINE")

package com.fueledbycaffeine.bunnypedia.ext.android

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

inline fun RecyclerView.ViewHolder.getString(@StringRes res: Int, vararg args: String): String {
  return itemView.context.getString(res, args)
}
