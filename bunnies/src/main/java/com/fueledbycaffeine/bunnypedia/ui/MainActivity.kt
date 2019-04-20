package com.fueledbycaffeine.bunnypedia.ui

import android.content.Intent
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.fueledbycaffeine.bunnypedia.R
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {
  private val navController get() = findNavController(this, R.id.navHostFragment)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    navController.handleDeepLink(intent)
  }

  override fun onSupportNavigateUp() = navController.navigateUp()
}
