package com.fueledbycaffeine.bunnypedia.database.model

import androidx.room.Embedded
import androidx.room.Relation

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
