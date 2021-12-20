package com.fueledbycaffeine.bunnypedia.ext.android

import android.view.Window
import androidx.core.view.WindowInsetsControllerCompat

inline var Window.isAppearanceLightStatusBars: Boolean
  get() = WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars
  set(isLight) { WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = isLight }
