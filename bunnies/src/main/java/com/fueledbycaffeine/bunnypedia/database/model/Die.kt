package com.fueledbycaffeine.bunnypedia.database.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.fueledbycaffeine.bunnypedia.R

enum class Die(val sides: Int) {
  RED(12),
  ORANGE(12),
  YELLOW(12),
  GREEN(12),
  BLUE(12),
  VIOLET(12),
  PINK(12),
  BLACK(12),
  BROWN(12),
  CLEAR(20),

  // Zodiacs
  ZODIAC(12),
  CHINESE_ZODIAC(12),

  // Dice from Kinder Bunnies:
  ORANGE_D10(10),
  YELLOW_D10(10),
  GREEN_D10(10),
  BLUE_D10(10),
  VIOLET_D10(10),
  ;

  val color: Int @ColorRes get() {
    return when (this) {
      RED -> R.color.deck_red
      ORANGE, ORANGE_D10 -> R.color.deck_orange
      YELLOW, YELLOW_D10 -> R.color.deck_yellow
      GREEN, GREEN_D10 -> R.color.deck_green
      BLUE, BLUE_D10 -> R.color.deck_blue
      VIOLET, VIOLET_D10 -> R.color.deck_violet
      PINK -> R.color.deck_pink
      BLACK, ZODIAC -> R.color.deck_onyx
      BROWN -> R.color.deck_chocolate
      CLEAR, CHINESE_ZODIAC -> R.color.white
    }
  }

  fun getDrawable(context: Context): Drawable? {
    val drawableRes = when (this) {
      CLEAR -> R.drawable.d20
      ZODIAC -> R.drawable.z12
      CHINESE_ZODIAC -> R.drawable.c12
      ORANGE_D10, YELLOW_D10, GREEN_D10, BLUE_D10, VIOLET_D10 -> R.drawable.d10
      else -> R.drawable.d12
    }
    val drawable = context.getDrawable(drawableRes)!!
    drawable.setTint(ContextCompat.getColor(context, color))
    return drawable
  }
}
