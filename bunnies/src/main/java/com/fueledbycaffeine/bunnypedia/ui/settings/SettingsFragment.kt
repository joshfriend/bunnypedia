package com.fueledbycaffeine.bunnypedia.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.preference.PreferenceFragmentCompat
import com.fueledbycaffeine.bunnypedia.R

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.preferences)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = super.onCreateView(inflater, container, savedInstanceState)
    view?.setBackgroundColor(requireContext().getColor(R.color.black))
    return view
  }

  override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
    view?.translationZ = if (nextAnim == R.anim.transition_exit && !enter) {
      0f
    } else {
      1f
    }

    return super.onCreateAnimation(transit, enter, nextAnim)
  }
}
