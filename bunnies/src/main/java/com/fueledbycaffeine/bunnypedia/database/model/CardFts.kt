package com.fueledbycaffeine.bunnypedia.database.model

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Card::class)
@Entity
class CardFts(
  val pk: String,
  val title: String
)
