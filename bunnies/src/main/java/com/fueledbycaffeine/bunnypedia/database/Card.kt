package com.fueledbycaffeine.bunnypedia.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
  val id: Int,
  private val canonicalId: Int? = null,
  val title: String,
  val deck: Deck,
  val type: CardType,
  val description: String = "",
  val additionalRules: String = "",
  val rules: List<RulesSection> = emptyList(),
  val rank: Rank? = null,
  val zodiacType: ZodiacType? = null,
  val bunnyRequirement: BunnyRequirement = BunnyRequirement.NOT_APPLICABLE,
  val dice: List<Die> = emptyList(),
  val symbols: List<Symbol> = emptyList(),
  val pawn: Pawn? = null,
  val weaponLevel: String? = null,
  val cabbage: Int = 0,
  val water: Int = 0,
  val psi: Psi? = null,
  val specialSeries: SpecialSeries? = null
): Parcelable {
  companion object {
    const val FTB_RANDOM = -1
    const val FTB_DATED= -2
  }

  val imageURI: String = "file:///android_asset/card_thumbnails/${String.format("%04d.png", canonicalId ?: id)}"

  val isFtb: Boolean = cabbage != 0 && water != 0
}
