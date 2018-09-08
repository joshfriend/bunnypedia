package com.fueledbycaffeine.bunnypedia.ext.android

import android.graphics.Paint
import android.os.Build
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import android.view.View

@RequiresApi(Build.VERSION_CODES.M)
fun View.useLightStatusBarStyle() {
  this.systemUiVisibility = this.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

@RequiresApi(Build.VERSION_CODES.M)
fun View.useDarkStatusBarStyle() {
  Paint.UNDERLINE_TEXT_FLAG
  this.systemUiVisibility = this.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
}

fun View.updatePaddingRelative(
  @Px start: Int = paddingStart,
  @Px top: Int = paddingTop,
  @Px end: Int = paddingEnd,
  @Px bottom: Int = paddingBottom
) {
  setPaddingRelative(start, top, end, bottom)
}
