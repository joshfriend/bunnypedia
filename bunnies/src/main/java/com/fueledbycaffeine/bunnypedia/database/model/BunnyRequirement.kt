package com.fueledbycaffeine.bunnypedia.database.model

import androidx.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R

enum class BunnyRequirement {
  NOT_APPLICABLE,
  NO,
  PLAY,
  PLAY_AND_SAVE,
  ;

  val description: Int @StringRes get() {
    return when (this) {
      PLAY -> R.string.bunny_required_to_play
      PLAY_AND_SAVE -> R.string.bunny_required_to_play_and_save
      else -> R.string.no
    }
  }
}
