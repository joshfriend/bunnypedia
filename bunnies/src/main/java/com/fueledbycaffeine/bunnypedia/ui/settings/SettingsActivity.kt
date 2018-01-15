package com.fueledbycaffeine.bunnypedia.ui.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fueledbycaffeine.bunnypedia.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    setTitle(R.string.settings)
  }
}
