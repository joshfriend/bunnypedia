package com.fueledbycaffeine.bunnypedia.database

import android.content.Context
import com.fueledbycaffeine.bunnypedia.R

enum class CardType {
  RUN,
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

  fun localizedDescription(context: Context): String {
    val stringRes = when (this) {
      RUN -> R.string.card_type_run
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
    return context.getString(stringRes)
  }
}
