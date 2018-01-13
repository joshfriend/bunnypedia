package com.fueledbycaffeine.bunnypedia.database.model

import android.support.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R

enum class Psi(val type: Type, val color: Color) {
  PSI01(Type.KITE, Color.BLUE),
  PSI02(Type.ARROW, Color.YELLOW),
  PSI03(Type.ARROW, Color.VIOLET),
  PSI04(Type.KITE, Color.GREEN),
  PSI05(Type.KITE, Color.RED),
  PSI06(Type.ARROW, Color.ORANGE),
  PSI07(Type.ARROW, Color.BLUE),
  PSI08(Type.KITE, Color.YELLOW),
  PSI09(Type.ARROW, Color.RED),
  PSI10(Type.ARROW, Color.GREEN),
  PSI11(Type.KITE, Color.VIOLET),
  PSI12(Type.KITE, Color.ORANGE),
  ;

  enum class Type {
    ARROW,
    KITE,
    ;

    val description: Int @StringRes get() {
      return when (this) {
        ARROW -> R.string.psi_type_arrow
        KITE -> R.string.psi_type_kite
      }
    }
  }

  enum class Color {
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    BLUE,
    VIOLET,
    ;

    val colorName: Int @StringRes get() {
      return when (this) {
        RED -> R.string.red
        ORANGE -> R.string.orange
        YELLOW -> R.string.yellow
        GREEN -> R.string.green
        BLUE -> R.string.blue
        VIOLET -> R.string.violet
      }
    }
  }
}
