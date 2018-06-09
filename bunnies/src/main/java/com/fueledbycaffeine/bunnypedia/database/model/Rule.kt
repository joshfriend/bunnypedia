package com.fueledbycaffeine.bunnypedia.database.model

import android.arch.persistence.room.*

@Entity(
  foreignKeys = [
    ForeignKey(
      entity = Card::class,
      parentColumns = ["id"],
      childColumns = ["cardId"],
      onDelete = ForeignKey.CASCADE
    )
  ]
)
data class Rule(
  @PrimaryKey
  val id: Int,

  @ColumnInfo(index = true)
  val cardId: Int,

  val title: String,
  val text: String
)
