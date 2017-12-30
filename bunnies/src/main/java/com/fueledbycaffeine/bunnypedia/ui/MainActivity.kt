package com.fueledbycaffeine.bunnypedia.ui

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment.Companion.ARG_CARD
import com.fueledbycaffeine.bunnypedia.ui.card.CardListFragment
import com.fueledbycaffeine.bunnypedia.ui.NavigationViewModel.Action
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailActivity
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.startActivity

class MainActivity: AppCompatActivity() {
  companion object {
    private const val TAG_CARD_LIST = "cardList"
  }

  private var navigationSubscriber: Disposable? = null
  private lateinit var navigation: NavigationViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

//    navigation = ViewModelProviders.of(this).get(NavigationViewModel::class.java)

//    observeNavigationCommands()

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
  }

  override fun onDestroy() {
    super.onDestroy()
    navigationSubscriber?.dispose()
  }
//
//  override fun onBackPressed() {
//    if (supportFragmentManager.backStackEntryCount > 1) {
//      navigation.goBack()
//    } else {
//      super.onBackPressed()
//    }
//  }

  private fun observeNavigationCommands() {
    navigationSubscriber?.dispose()
    navigationSubscriber = navigation.actions.subscribe { action ->
      when (action) {
        is Action.ShowList -> {

        }
        is Action.ShowCard -> {

        }
        is Action.GoBack -> {

        }
      }
    }
  }
}
