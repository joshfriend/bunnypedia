package com.fueledbycaffeine.bunnypedia.util

import android.graphics.Color
import androidx.annotation.ColorInt

object ColorUtil {
  fun getLuminance(@ColorInt color: Int): Double {
    return 1 - (
      0.299 * Color.red(color)
        + 0.587 * Color.green(color)
        + 0.114 * Color.blue(color)
      ) / 255
  }

  @ColorInt fun contrastColor(@ColorInt color: Int): Int {
    val luminance = getLuminance(color)
    val white = if (luminance < 0.5) 0 else 255
    return Color.argb(255, white, white, white)
  }

  @ColorInt fun darkenColor(@ColorInt color: Int, byAmount: Float): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(color, hsv)
    hsv[2] *= (1 - byAmount)
    return Color.HSVToColor(hsv)
  }
}
