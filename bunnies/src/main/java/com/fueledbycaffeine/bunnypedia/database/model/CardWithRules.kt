package com.fueledbycaffeine.bunnypedia.database.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class CardWithRules {
  @Embedded
  lateinit var card: Card

  @Relation(
    parentColumn = "id",
    entityColumn = "cardId"
  )
  var rules: List<Rule> = emptyList()

  operator fun component1() = card
  operator fun component2() = rules
}
