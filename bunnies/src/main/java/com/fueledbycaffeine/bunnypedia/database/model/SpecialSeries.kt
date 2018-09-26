package com.fueledbycaffeine.bunnypedia.database.model

import androidx.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R

enum class SpecialSeries {
  OMEGA,
  PSI,
  NU,
  THETA,
  PHI,
  PI,
  ;

  val symbol: String get() {
    return when (this) {
      OMEGA -> "Ω"
      PSI -> "Ψ"
      NU -> "Ν"
      THETA -> "ϴ"
      PHI -> "Φ"
      PI -> "π"
    }
  }

  val title: Int @StringRes get() {
    return when (this) {
      OMEGA -> R.string.series_omega
      PSI -> R.string.series_psi
      NU -> R.string.series_nu
      THETA -> R.string.series_theta
      PHI -> R.string.series_phi
      PI -> R.string.series_pi
    }
  }
}
