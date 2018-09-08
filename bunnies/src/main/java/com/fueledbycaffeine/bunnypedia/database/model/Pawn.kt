package com.fueledbycaffeine.bunnypedia.database.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R

enum class Pawn {
  RED,
  ORANGE,
  YELLOW,
  GREEN,
  BLUE,
  VIOLET,
  PINK,
  BLACK,
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
      BLACK -> R.color.deck_onyx
    }
  }

  val pawnName: Int @StringRes get() {
    return when (this) {
      RED -> R.string.red
      ORANGE -> R.string.orange
      YELLOW -> R.string.yellow
      GREEN -> R.string.green
      BLUE -> R.string.blue
      VIOLET -> R.string.violet
      PINK -> R.string.pink
      BLACK -> R.string.black
    }
  }
}
