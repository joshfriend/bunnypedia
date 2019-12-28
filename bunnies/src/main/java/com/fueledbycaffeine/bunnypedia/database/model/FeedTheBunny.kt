package com.fueledbycaffeine.bunnypedia.database.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedTheBunny(
  val cabbage: Int,
  val radish: Int,
  val water: Int,
  val milk: Int
) : Parcelable {
  companion object {
    const val RANDOM = -1
    const val DATED = -2
  }

  val applicable get() = cabbage != 0 || radish != 0 || water != 0 || milk != 0

  val cabbageAndWater get() = cabbage > 0 && water > 0
  val radishAndMilk get() = radish > 0 && milk > 0
  val cabbageOrRadish get() = cabbage > 0 && radish > 0
  val waterOrMilk get() = water > 0 && milk > 0
}
