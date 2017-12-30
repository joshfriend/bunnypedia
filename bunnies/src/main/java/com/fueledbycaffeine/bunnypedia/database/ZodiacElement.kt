package com.fueledbycaffeine.bunnypedia.database

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R

enum class ZodiacElement {
  FIRE,
  EARTH,
  AIR,
  WATER,
  ;

  val description: Int @StringRes get() {
    return when (this) {
      FIRE -> R.string.zodiac_type_fire
      EARTH -> R.string.zodiac_type_earth
      AIR -> R.string.zodiac_type_air
      WATER -> R.string.zodiac_type_water
    }
  }

  val symbol: Int @StringRes get() {
    return when (this) {
      FIRE -> R.drawable.zodiac_element_fire
      EARTH -> R.drawable.zodiac_element_earth
      AIR -> R.drawable.zodiac_element_air
      WATER -> R.drawable.zodiac_element_water
    }
  }

  val tintColor: Int @ColorRes get() {
    return when (this) {
      FIRE -> R.color.zodiac_element_fire
      EARTH -> R.color.zodiac_element_earth
      AIR -> R.color.zodiac_element_air
      WATER -> R.color.zodiac_element_water
    }
  }
}
