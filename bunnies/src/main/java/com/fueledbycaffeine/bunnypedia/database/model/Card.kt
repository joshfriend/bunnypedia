package com.fueledbycaffeine.bunnypedia.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  indices = [
    Index(unique = true, value = ["id"])
  ],
  foreignKeys = [
    ForeignKey(
      entity = Card::class,
      parentColumns = ["id"],
      childColumns = ["canonicalId"]
    )
  ]
)
data class Card(
  @PrimaryKey
  val pk: Int,

  val id: String,

  @ColumnInfo(index = true)
  val canonicalId: String?,

  val title: String,
  val deck: Deck,
  val type: CardType,
  val rank: Rank?,
  val zodiacSign: ZodiacSign?,
  val zodiacAnimal: ZodiacAnimal?,
  val bunnyRequirement: BunnyRequirement?,
  val dice: List<Die>?,
  val symbols: List<Symbol>?,
  val pawn: Pawn?,
  val weaponLevel: String?,
  val psi: Psi?,
  val specialSeries: SpecialSeries?,

  @Embedded
  val ftb: FeedTheBunny
) {
  val imageURI: String get() = "file:///android_asset/card_thumbnails/${canonicalId ?: id}.jpg"
}
