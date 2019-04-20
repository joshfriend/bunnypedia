package com.fueledbycaffeine.bunnypedia.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
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
  val id: Int,

  @ColumnInfo(index = true)
  val canonicalId: Int?,

  val title: String,
  val deck: Deck,
  val type: CardType,
  val rank: Rank?,
  val zodiacType: ZodiacType?,
  val zodiacAnimal: ZodiacAnimal?,
  val bunnyRequirement: BunnyRequirement,
  val dice: List<Die>,
  val symbols: List<Symbol>,
  val pawn: Pawn?,
  val weaponLevel: String?,
  val psi: Psi?,
  val specialSeries: SpecialSeries?,

  @Embedded
  val ftb: FeedTheBunny
) {
  companion object {
    const val FTB_RANDOM = -1
    const val FTB_DATED = -2
  }

  val imageURI: String get() = "file:///android_asset/card_thumbnails/${String.format("%04d.jpg", canonicalId ?: id)}"
}
