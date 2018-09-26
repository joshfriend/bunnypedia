package com.fueledbycaffeine.bunnypedia.database.model

import androidx.annotation.ColorRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R

enum class Deck {
  BLUE,
  YELLOW,
  RED,
  VIOLET,
  ORANGE,
  GREEN,
  TWILIGHT_WHITE,
  STAINLESS_STEEL,
  PERFECTLY_PINK,
  WACKY_KHAKI,
  OMINOUS_ONYX,
  CHOCOLATE,
  FANTASTIC,
  CARAMEL_SWIRL,
  CREATURE_FEATURE,
  PUMPKIN_SPICE,
  ;

  val color: Int @ColorRes get() {
    return when (this) {
      RED -> R.color.deck_red
      ORANGE -> R.color.deck_orange
      YELLOW -> R.color.deck_yellow
      GREEN -> R.color.deck_green
      BLUE -> R.color.deck_blue
      VIOLET -> R.color.deck_violet
      PERFECTLY_PINK -> R.color.deck_pink
      TWILIGHT_WHITE -> R.color.deck_white
      STAINLESS_STEEL -> R.color.deck_steel
      OMINOUS_ONYX -> R.color.deck_onyx
      WACKY_KHAKI -> R.color.deck_khaki
      CHOCOLATE -> R.color.deck_chocolate
      FANTASTIC -> R.color.deck_fantastic
      CARAMEL_SWIRL -> R.color.deck_caramel
      CREATURE_FEATURE -> R.color.deck_creature_feature
      PUMPKIN_SPICE -> R.color.deck_pumpkin_spice
    }
  }

  val description: Int @StringRes get() {
    return when (this) {
      RED -> R.string.deck_red_booster
      ORANGE -> R.string.deck_orange_booster
      YELLOW -> R.string.deck_yellow_booster
      GREEN -> R.string.deck_green_booster
      BLUE -> R.string.deck_blue_starter
      VIOLET -> R.string.deck_violet_booster
      PERFECTLY_PINK -> R.string.deck_perfectly_pink_booster
      TWILIGHT_WHITE -> R.string.deck_twilight_white_booster
      STAINLESS_STEEL -> R.string.deck_stainless_steel_booster
      OMINOUS_ONYX -> R.string.deck_ominous_onyx_booster
      WACKY_KHAKI -> R.string.deck_wacky_khaki_booster
      CHOCOLATE -> R.string.deck_chocolate_booster
      FANTASTIC -> R.string.deck_fantastic_booster
      CARAMEL_SWIRL -> R.string.deck_caramel_swirl_booster
      CREATURE_FEATURE -> R.string.deck_creature_feature_booster
      PUMPKIN_SPICE -> R.string.deck_pumpkin_spice_booster
    }
  }
}
