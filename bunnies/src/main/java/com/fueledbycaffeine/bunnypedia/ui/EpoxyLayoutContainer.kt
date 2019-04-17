@file:Suppress("HasPlatformType", "NOTHING_TO_INLINE")

package com.fueledbycaffeine.bunnypedia.ui

import android.view.View
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyHolder

/**
 * Binds the [itemView] passed to [bindView] into the instance so it can be used later
 * to access kotlinx synthetic view properties.
 */
abstract class EpoxyLayoutContainer : EpoxyHolder() {
  lateinit var itemView: View
    private set

  override fun bindView(itemView: View) {
    this.itemView = itemView
  }

  inline val context get() = itemView.context
  inline fun getString(@StringRes res: Int) = context.getString(res)
  @Suppress("SpreadOperator")
  inline fun getString(@StringRes res: Int, vararg args: Any) = context.getString(res, *args)
}
