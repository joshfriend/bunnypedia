package com.fueledbycaffeine.bunnypedia.ui.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import com.fueledbycaffeine.bunnypedia.R

class SettingsFragment: PreferenceFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.preferences)
  }
}
