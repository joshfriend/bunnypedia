package com.fueledbycaffeine.bunnypedia.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CardWithRules(
  @Embedded
  val card: Card,

  @Relation(
    parentColumn = "id",
    entityColumn = "cardId"
  )
  val rules: List<Rule> = emptyList()
)
