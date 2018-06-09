package com.fueledbycaffeine.bunnypedia.ext.android

import android.app.Activity
import android.os.Build

/**
 * Whether the activity is in multi-window mode
 *
 * Always false on pre-N devices
 */
inline val Activity.isInMultiWindow: Boolean get() {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    isInMultiWindowMode
  } else {
    false
  }
}
