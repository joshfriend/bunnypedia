package com.fueledbycaffeine.bunnypedia.ui.card

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.Database
import com.fueledbycaffeine.bunnypedia.ui.NavigationViewModel
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment.Companion.ARG_CARD
import kotlinx.android.synthetic.main.fragment_card_list.*
import org.jetbrains.anko.support.v4.startActivity
import timber.log.Timber

class CardListFragment: Fragment() {
  private lateinit var database: Database
  private lateinit var navigation: NavigationViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    database = Database(activity!!)
    navigation = ViewModelProviders.of(activity!!).get(NavigationViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_card_list, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    if (savedInstanceState == null) {
      recyclerView.adapter = CardAdapter(database.allCards) { card ->
        startActivity<CardDetailActivity>(ARG_CARD to card)
      }
//    }
  }
}
