package com.fueledbycaffeine.bunnypedia.database

import android.support.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R

enum class CardType {
  RUN,
  ROAMING_RED_RUN,
  SPECIAL,
  VERY_SPECIAL,
  DOLLA,
  PLAY_IMMEDIATELY,
  ZODIAC,
  MYSTERIOUS_PLACE,
  RANK,
  CARROT_SUPPLY,
  STARTER,
  ;

  val description: Int @StringRes get() {
    return when (this) {
      RUN -> R.string.card_type_run
      ROAMING_RED_RUN -> R.string.card_type_roaming_red_run
      SPECIAL -> R.string.card_type_special
      VERY_SPECIAL -> R.string.card_type_very_special
      DOLLA -> R.string.card_type_kaballa_dolla
      PLAY_IMMEDIATELY -> R.string.card_type_play_immediately
      ZODIAC -> R.string.card_type_zodiac
      MYSTERIOUS_PLACE -> R.string.card_type_mysterious_place
      RANK -> R.string.card_type_rank
      CARROT_SUPPLY -> R.string.card_type_carrot_supply
      STARTER -> R.string.card_type_play_immediately
    }
  }
}
