package com.fueledbycaffeine.bunnypedia.database.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R


enum class RuleType {
  NONE,
  COUNTER,
  RELATED,
  ;

  val text @StringRes get() = when (this) {
    NONE -> -1
    COUNTER -> R.string.rule_type_counter
    RELATED -> R.string.rule_type_related
  }

  val color @ColorRes get() = when (this) {
    NONE -> -1
    COUNTER -> R.color.deck_red
    RELATED -> R.color.deck_blue
  }
}
