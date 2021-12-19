package com.fueledbycaffeine.bunnypedia.ext.android

import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsControllerCompat

inline var Window.isAppearanceLightStatusBars: Boolean
  get() = WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars
  set(isLight) { WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = isLight }

@RequiresApi(Build.VERSION_CODES.M)
fun View.useLightStatusBarStyle() {
  @Suppress("DEPRECATION")
  systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

@RequiresApi(Build.VERSION_CODES.M)
fun View.useDarkStatusBarStyle() {
  @Suppress("DEPRECATION")
  systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
}
