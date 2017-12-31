package com.fueledbycaffeine.bunnypedia.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.ui.card.CardListFragment
import com.fueledbycaffeine.bunnypedia.ui.NavigationViewModel.Action
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.searchManager
import android.app.SearchManager
import timber.log.Timber


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

    handleIntent(intent)
  }

//  override fun onCreateOptionsMenu(menu: Menu): Boolean {
//    menuInflater.inflate(R.menu.main_options_menu, menu)
//
//    // Associate searchable configuration with the SearchView
//    val searchView = menu.findItem(R.id.search).actionView as SearchView
//    searchView.setSearchableInfo(
//      searchManager.getSearchableInfo(componentName)
//    )
//
//    return true
//  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    handleIntent(intent)
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

  private fun handleIntent(intent: Intent) {
    if (intent.action == Intent.ACTION_SEARCH) {
      val query = intent.getStringExtra(SearchManager.QUERY)
      Timber.d("Searched for: '$query'")
    }
  }

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
