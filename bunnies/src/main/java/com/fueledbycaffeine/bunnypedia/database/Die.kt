package com.fueledbycaffeine.bunnypedia.database

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
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
  ZODIAC(12),
  ;

  val color: Int @ColorRes get() {
    return when (this) {
      RED -> R.color.deck_red
      ORANGE -> R.color.deck_orange
      YELLOW -> R.color.deck_yellow
      GREEN -> R.color.deck_green
      BLUE -> R.color.deck_blue
      VIOLET -> R.color.deck_violet
      PINK -> R.color.deck_pink
      BLACK, ZODIAC -> R.color.deck_onyx
      BROWN -> R.color.deck_chocolate
      CLEAR -> R.color.white
    }
  }

  fun getDrawable(context: Context): Drawable {
    val drawableRes = when (this) {
      CLEAR -> R.drawable.dice20
      ZODIAC -> R.drawable.dice12  // TODO
      else -> R.drawable.dice12
    }
    val drawable = ContextCompat.getDrawable(context, drawableRes)!!
    drawable.setTint(ContextCompat.getColor(context, color))
    return drawable
  }
}
