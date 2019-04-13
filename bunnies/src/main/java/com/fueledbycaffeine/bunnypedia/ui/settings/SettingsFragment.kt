package com.fueledbycaffeine.bunnypedia.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.fueledbycaffeine.bunnypedia.R

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.preferences)
  }
}
