package com.fueledbycaffeine.bunnypedia.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
  val id: Int,
  private val canonicalId: Int?,
  val title: String,
  val description: String,
  val deck: Deck,
  val hide: Boolean,
  val type: CardType,
  val zodiacType: ZodiacType?,
  val requiresBunny: Boolean?,
  val dice: List<Die>,
  val pawn: Pawn?,
  val weaponLevel: String?,
  val cabbage: Int,
  val water: Int
): Parcelable {
  companion object {
    const val FTB_RANDOM = -1
    const val FTB_DATED= -2
  }

  val imageURI: String = "file:///android_asset/card_thumbnails/${String.format("%04d.png", canonicalId ?: id)}"

  val isFtb: Boolean = cabbage != 0 && water != 0
}
