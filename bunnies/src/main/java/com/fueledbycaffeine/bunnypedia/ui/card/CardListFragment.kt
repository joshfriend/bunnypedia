package com.fueledbycaffeine.bunnypedia.ui.card

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.*
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.Card
import com.fueledbycaffeine.bunnypedia.database.Database
import com.fueledbycaffeine.bunnypedia.database.Deck
import com.fueledbycaffeine.bunnypedia.injection.App
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment.Companion.ARG_CARD
import com.fueledbycaffeine.bunnypedia.ui.settings.SettingsActivity
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_card_list.*
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.startActivityForResult
import timber.log.Timber
import javax.inject.Inject


class CardListFragment: Fragment() {
  companion object {
    private const val REQ_SETTINGS = 1
  }

  @Inject lateinit var database: Database
  private lateinit var adapter: CardAdapter
  private var optionsMenuSubscribers = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    App.graph.inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_card_list, container, false)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.main_options_menu, menu)
    val searchMenuItem = menu.findItem(R.id.search)
    val search = searchMenuItem.actionView as SearchView
    search.queryHint = getString(R.string.search_hint)

    val queryEvents = search.queryTextChangeEvents().share()

    queryEvents.map { it.queryText().toString().trim() }
      .subscribeBy(onNext = { adapter.query = it })
      .addTo(this.optionsMenuSubscribers)

    queryEvents.filter { it.isSubmitted }
      .map { it.queryText().toString().trim() }
      .map { it.toIntOrNull() ?: -1 }
      .subscribeBy(
        onNext = { id ->
          val card = database.getCard(id)
          if (card != null) {
            search.setQuery("", false)
            searchMenuItem.collapseActionView()
            this.onCardSelected(card)
          }
        },
        onError = Timber::e
      )
      .addTo(this.optionsMenuSubscribers)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val activity = activity
    if (activity is AppCompatActivity) {
      activity.setSupportActionBar(toolbar)
    }

    adapter = CardAdapter(emptyList(), this::onCardSelected)
    recyclerView.adapter = adapter

    database.getAllCards()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { cards ->
        adapter = CardAdapter(cards, this::onCardSelected)
        adapter.shownDecks = getAvailableDecks()
        loadingView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        recyclerView.adapter = adapter
      }

  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.settings -> {
        startActivityForResult<SettingsActivity>(REQ_SETTINGS)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
      REQ_SETTINGS -> adapter.shownDecks = getAvailableDecks()
      else -> super.onActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onDestroyOptionsMenu() {
    this.optionsMenuSubscribers.clear()
    super.onDestroyOptionsMenu()
  }

  private fun onCardSelected(card: Card) {
    startActivity<CardDetailActivity>(ARG_CARD to card)
  }

  private fun getAvailableDecks(): Set<Deck> {
    return defaultSharedPreferences.getStringSet(
      getString(R.string.pref_key_booster_decks),
      resources.getStringArray(R.array.pref_all_booster_decks_values).toSet()
    ).map { Deck.valueOf(it) }.toSet() + setOf(Deck.BLUE)
  }
}
