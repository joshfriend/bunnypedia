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
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment.Companion.ARG_CARD
import com.fueledbycaffeine.bunnypedia.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_card_list.*
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.startActivityForResult


class CardListFragment: Fragment(), SearchView.OnQueryTextListener {
  companion object {
    private const val REQ_SETTINGS = 1
  }

  private lateinit var database: Database
  private lateinit var adapter: CardAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)

    database = Database(activity!!)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_card_list, container, false)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.main_options_menu, menu)
    val search = menu.findItem(R.id.search).actionView as SearchView
    search.setOnQueryTextListener(this)
    search.queryHint = getString(R.string.search_hint)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val activity = activity
    if (activity is AppCompatActivity) {
      activity.setSupportActionBar(toolbar)
    }

    adapter = CardAdapter(database.allCards, this::onCardSelected)
    adapter.shownDecks = getAvailableDecks()
    recyclerView.adapter = adapter
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

  override fun onQueryTextSubmit(query: String?): Boolean {
    val id = query?.trim()?.toIntOrNull()
    if (id != null) {
      database.getCard(id)?.let { onCardSelected(it) }
    }
    return true
  }

  override fun onQueryTextChange(newText: String?): Boolean {
    adapter.query = newText?.trim()
    return true
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
