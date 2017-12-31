package com.fueledbycaffeine.bunnypedia.database

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.RankType.*

enum class Rank(val type: RankType, val grade: Int) {
  // Enlisted
  E1(ENLISTED, 1),  // Seaman Recruit
  E2(ENLISTED, 2),  // Seaman Apprentice
  E3(ENLISTED, 3),  // Seaman
  E4(ENLISTED, 4),  // Petty Officer 3rd Class
  E5(ENLISTED, 5),  // Petty Officer 2nd Class
  E6(ENLISTED, 6),  // Petty Officer 1st Class
  E7(ENLISTED, 7),  // Chief Petty Officer
  E8(ENLISTED, 8),  // Senior Chief Petty Officer
  E9(ENLISTED, 9),  // Master Chief Petty Officer
  // Officer
  O1(OFFICER, 1),  // Ensign
  O2(OFFICER, 2),  // Lieutenant JG
  O3(OFFICER, 3),  // Lieutenant
  O4(OFFICER, 4),  // Lieutenant Commander
  O5(OFFICER, 5),  // Commander
  O6(OFFICER, 6),  // Captain
  O7(OFFICER, 7),  // Lower Rear Admiral
  O8(OFFICER, 8),  // Upper Rear Admiral
  O9(OFFICER, 9),  // Vice Admiral
  ;

  val description: Int @StringRes get() {
    return when (this) {
      E1 -> R.string.rank_e1
      E2 -> R.string.rank_e2
      E3 -> R.string.rank_e3
      E4 -> R.string.rank_e4
      E5 -> R.string.rank_e5
      E6 -> R.string.rank_e6
      E7 -> R.string.rank_e7
      E8 -> R.string.rank_e8
      E9 -> R.string.rank_e9
      O1 -> R.string.rank_01
      O2 -> R.string.rank_02
      O3 -> R.string.rank_03
      O4 -> R.string.rank_04
      O5 -> R.string.rank_05
      O6 -> R.string.rank_06
      O7 -> R.string.rank_07
      O8 -> R.string.rank_08
      O9 -> R.string.rank_09
    }
  }

  val symbol: Int @DrawableRes get() {
    return when (this) {
      E1 -> R.drawable.rank_e1
      E2 -> R.drawable.rank_e2
      E3 -> R.drawable.rank_e3
      E4 -> R.drawable.rank_e4
      E5 -> R.drawable.rank_e5
      E6 -> R.drawable.rank_e6

      // TODO:
      E7 -> R.drawable.rank_e1
      E8 -> R.drawable.rank_e1
      E9 -> R.drawable.rank_e1
      O1 -> R.drawable.rank_e1
      O2 -> R.drawable.rank_e1
      O3 -> R.drawable.rank_e1
      O4 -> R.drawable.rank_e1
      O5 -> R.drawable.rank_e1
      O6 -> R.drawable.rank_e1
      O7 -> R.drawable.rank_e1
      O8 -> R.drawable.rank_e1
      O9 -> R.drawable.rank_e1
    }
  }
}
