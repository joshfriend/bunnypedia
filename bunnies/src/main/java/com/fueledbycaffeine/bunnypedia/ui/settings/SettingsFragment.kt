package com.fueledbycaffeine.bunnypedia.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import androidx.core.content.IntentCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.AppDatabase
import com.fueledbycaffeine.bunnypedia.ui.MainActivity
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.preferences)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val resetCardDb = preferenceManager.findPreference<Preference>(
      getString(R.string.pref_key_reset_card_db)
    )
    resetCardDb?.setOnPreferenceClickListener {
      requireContext().getDatabasePath(AppDatabase.DATABASE_NAME).delete()
      activity?.also {
        it.finish()
        startActivity(Intent.makeRestartActivityTask(it.componentName))
      }
      true
    }
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
