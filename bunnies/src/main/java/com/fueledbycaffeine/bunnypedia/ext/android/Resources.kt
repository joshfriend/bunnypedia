package com.fueledbycaffeine.bunnypedia.ext.android

import android.content.res.Resources
import android.support.annotation.Px
import com.fueledbycaffeine.bunnypedia.R

/**
 * A tablet is a device where the smallest screen edge is at least 600dp
 */
inline val Resources.isTablet: Boolean get() = getBoolean(R.bool.is_tablet)

/**
 * Indicates if the navigation bar is being shown at the bottom of the screen.
 */
inline val Resources.isNavBarAtBottom: Boolean get() {
  // Navbar is always on the bottom of the screen in portrait mode, but may
  // rotate with device if its category is sw600dp or above.
  return this.isTablet || this.configuration.isPortrait
}

/**
 * How tall the navigation bar is.
 *
 * @return 0 if navbar is hardware, or the height of the software bar
 */
val Resources.navBarHeight: Int @Px get() {
  val id = getIdentifier("navigation_bar_height", "dimen", "android")
  return when {
    id > 0 -> getDimensionPixelSize(id)
    else -> 0
  }
}

val Resources.statusBarHeight: Int @Px get() {
  val id = getIdentifier("status_bar_height", "dimen", "android")
  return when {
    id > 0 -> getDimensionPixelSize(id)
    else -> 0
  }
}

/**
 * Whether the device has (and is showing) a software navigation bar or not
 */
val Resources.showsSoftwareNavBar: Boolean get() {
  val id = getIdentifier("config_showNavigationBar", "bool", "android")
  return id > 0 && getBoolean(id)
}
