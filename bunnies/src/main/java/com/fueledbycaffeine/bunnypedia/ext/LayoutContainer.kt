@file:Suppress("HasPlatformType", "NOTHING_TO_INLINE")

package com.fueledbycaffeine.bunnypedia.ext

import androidx.annotation.StringRes
import kotlinx.android.extensions.LayoutContainer

inline val LayoutContainer.context get() = containerView!!.context
inline fun LayoutContainer.getString(@StringRes res: Int) = context.getString(res)
inline fun LayoutContainer.getString(@StringRes res: Int, vararg args: Any) = context.getString(res, args)
