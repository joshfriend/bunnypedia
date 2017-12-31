package com.fueledbycaffeine.bunnypedia.ui.card

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.Card
import com.fueledbycaffeine.bunnypedia.database.Database
import com.fueledbycaffeine.bunnypedia.ui.MainActivity
import com.fueledbycaffeine.bunnypedia.ui.NavigationViewModel
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment.Companion.ARG_CARD
import kotlinx.android.synthetic.main.fragment_card_list.*
import org.jetbrains.anko.support.v4.startActivity


class CardListFragment: Fragment(), SearchView.OnQueryTextListener {
  private lateinit var database: Database
  private lateinit var navigation: NavigationViewModel
  private lateinit var adapter: CardAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)

    database = Database(activity!!)
    navigation = ViewModelProviders.of(activity!!).get(NavigationViewModel::class.java)
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
    if (activity is MainActivity) {
      activity.setSupportActionBar(toolbar)
    }

    adapter = CardAdapter(database.allCards, this::onCardSelected)
    recyclerView.adapter = adapter
  }

  override fun onQueryTextSubmit(query: String?): Boolean {
    val id = query?.toIntOrNull()
    if (id != null) {
      val card = database.getCard(id)
      if (card != null) {
        onCardSelected(card)
      }
    }
    return true
  }

  override fun onQueryTextChange(newText: String?): Boolean {
    adapter.setQuery(newText)
    return true
  }

  private fun onCardSelected(card: Card) {
    startActivity<CardDetailActivity>(ARG_CARD to card)
  }
}
