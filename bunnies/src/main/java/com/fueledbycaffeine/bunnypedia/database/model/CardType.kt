package com.fueledbycaffeine.bunnypedia.database.model

import androidx.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R

enum class CardType {
  RUN,
  ROAMING_RED_RUN,
  SPECIAL,
  VERY_SPECIAL,
  DOLLA,
  METAL,
  PLAY_IMMEDIATELY,
  ZODIAC,
  CHINESE_ZODIAC,
  MYSTERIOUS_PLACE,
  RANK,
  SENATOR,
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
      METAL -> R.string.card_type_metal
      PLAY_IMMEDIATELY -> R.string.card_type_play_immediately
      ZODIAC -> R.string.card_type_zodiac
      CHINESE_ZODIAC -> R.string.card_type_chinese_zodiac
      MYSTERIOUS_PLACE -> R.string.card_type_mysterious_place
      RANK -> R.string.card_type_rank
      SENATOR -> R.string.card_type_senator
      CARROT_SUPPLY -> R.string.card_type_carrot_supply
      STARTER -> R.string.card_type_play_immediately
    }
  }
}
