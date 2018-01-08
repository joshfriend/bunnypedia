package com.fueledbycaffeine.bunnypedia.ext.android

import android.content.res.Resources
import com.fueledbycaffeine.bunnypedia.R

val Resources.isTablet: Boolean get() = getBoolean(R.bool.is_tablet)

val Resources.navBarHeight: Int get() {
  val id = getIdentifier("navigation_bar_height", "dimen", "android")
  return if (id > 0) {
    getDimensionPixelSize(id)
  } else {
    0
  }
}

val Resources.statusBarHeight: Int get() {
  val id = getIdentifier("status_bar_height", "dimen", "android")
  return if (id > 0) {
    getDimensionPixelSize(id)
  } else {
    0
  }
}

val Resources.hasNavBar: Boolean get() {
  val id = getIdentifier("config_showNavigationBar", "bool", "android")
  return id > 0 && getBoolean(id)
}
