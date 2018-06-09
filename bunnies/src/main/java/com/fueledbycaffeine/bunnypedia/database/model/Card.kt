package com.fueledbycaffeine.bunnypedia.database.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Card(
  @PrimaryKey
  val id: Int,

  val canonicalId: Int?,
  val title: String,
  val deck: Deck,
  val type: CardType,
  val rank: Rank?,
  val zodiacType: ZodiacType?,
  val bunnyRequirement: BunnyRequirement,
  val dice: List<Die>,
  val symbols: List<Symbol>,
  val pawn: Pawn?,
  val weaponLevel: String?,
  val cabbage: Int,
  val water: Int,
  val psi: Psi?,
  val specialSeries: SpecialSeries?
) {
  companion object {
    const val FTB_RANDOM = -1
    const val FTB_DATED= -2
  }

//  @Relation(parentColumn = "id", entityColumn = "cardId")
//  var rules: List<Rule> = emptyList()

  val imageURI: String get() = "file:///android_asset/card_thumbnails/${String.format("%04d.png", canonicalId ?: id)}"

  val isFtb: Boolean get() = cabbage != 0 && water != 0
}
