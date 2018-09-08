package com.fueledbycaffeine.bunnypedia.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.ui.card.CardListFragment
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import android.app.SearchManager
import timber.log.Timber


class MainActivity: AppCompatActivity() {
  companion object {
    private const val TAG_CARD_LIST = "cardList"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (savedInstanceState == null) {
      val fragment = CardListFragment()
      supportFragmentManager.beginTransaction()
        .replace(R.id.content_frame, fragment, TAG_CARD_LIST)
        .commit()
    }

    val statusbarColor = ColorUtil.darkenColor(
      ContextCompat.getColor(this, R.color.deck_blue),
      byAmount = 0.20f
    )
    window.statusBarColor = statusbarColor

    handleIntent(intent)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    handleIntent(intent)
  }

  private fun handleIntent(intent: Intent) {
    if (intent.action == Intent.ACTION_SEARCH) {
      val query = intent.getStringExtra(SearchManager.QUERY)
      Timber.d("Searched for: '$query'")
    }
  }
}
