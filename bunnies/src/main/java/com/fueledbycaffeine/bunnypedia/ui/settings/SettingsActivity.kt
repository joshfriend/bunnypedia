package com.fueledbycaffeine.bunnypedia.ui.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fueledbycaffeine.bunnypedia.R

class SettingsActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    setTitle(R.string.settings)
  }
}
