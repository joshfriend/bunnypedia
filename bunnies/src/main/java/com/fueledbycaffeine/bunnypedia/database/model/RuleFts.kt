package com.fueledbycaffeine.bunnypedia.database.model

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Rule::class)
@Entity
class RuleFts(
  val title: String,
  val text: String
)
