@file:Suppress("DEPRECATION")

package com.fueledbycaffeine.bunnypedia.ui.card.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.CardStore
import com.fueledbycaffeine.bunnypedia.database.QueryResult
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.ext.android.defaultSharedPreferences
import com.fueledbycaffeine.bunnypedia.ext.android.hideSoftKeyboard
import com.fueledbycaffeine.bunnypedia.ext.android.isAppearanceLightStatusBars
import com.fueledbycaffeine.bunnypedia.ext.rx.mapToResult
import com.fueledbycaffeine.bunnypedia.ui.AboutDialogFragment
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import com.jakewharton.rxbinding3.appcompat.queryTextChangeEvents
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_card_list.*
import timber.log.Timber
import javax.inject.Inject

class CardListFragment : DaggerFragment() {
  @Inject lateinit var cardStore: CardStore
  @Inject lateinit var cardsViewModelProvider: CardsViewModel.Provider
  private val cardsViewModel: CardsViewModel by viewModels { cardsViewModelProvider }

  private lateinit var adapter: CardAdapter
  private var optionsMenuSubscribers = CompositeDisposable()
  private var subscribers = CompositeDisposable()

  private var viewType: CardAdapter.CardViewType
    get() = CardAdapter.CardViewType.valueOf(
      defaultSharedPreferences.getString(
        getString(R.string.pref_key_view_type),
        getString(R.string.pref_default_view_type)
      )!!
    )
    set(value) {
      defaultSharedPreferences.edit()
        .putString(getString(R.string.pref_key_view_type), value.name)
        .apply()
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setHasOptionsMenu(true)
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

    queryEvents.map { it.queryText.toString().trim() }
      .subscribe { cardsViewModel.setQuery(it) }
      .addTo(this.optionsMenuSubscribers)

    queryEvents.filter { it.isSubmitted }
      .map { it.queryText.toString().trim() }
      .observeOn(Schedulers.io())
      .flatMapSingle { cardId ->
        cardStore.findCard(cardId).mapToResult()
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onNext = { result ->
          hideSoftKeyboard()
          when (result) {
            is QueryResult.Found<*> -> {
              @Suppress("UNCHECKED_CAST")
              val items = result.item as List<CardWithRules>
              if (items.size == 1) {
                search.setQuery("", false)
                searchMenuItem.collapseActionView()
                this.onCardSelected(items.first().card.id)
              }
            }
            is QueryResult.Error -> Timber.w("${result.error}")
          }
        },
        onError = Timber::e
      )
      .addTo(this.optionsMenuSubscribers)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    activity?.window?.isAppearanceLightStatusBars = false
    val activity = activity
    if (activity is AppCompatActivity) {
      activity.setSupportActionBar(toolbar)
    }

    // TODO: https://github.com/bumptech/glide/tree/master/integration/recyclerview
    adapter = CardAdapter(this, viewType, this::onCardSelected)
    recyclerView.adapter = adapter
    recyclerView.itemAnimator = null
    setupLayoutManager()

    fastScroller.setRecyclerView(recyclerView)

    cardsViewModel.observe()
      .subscribe { data ->
        data.observe(this.viewLifecycleOwner, adapter::submitList)
      }
      .addTo(this.subscribers)
  }

  override fun onPrepareOptionsMenu(menu: Menu) {
    val isGrid = viewType == CardAdapter.CardViewType.GRID
    menu.findItem(R.id.viewList).isVisible = isGrid
    menu.findItem(R.id.viewGrid).isVisible = !isGrid
  }

  override fun onResume() {
    super.onResume()

    cardsViewModel.reloadAvailableDecks()

    val statusbarColor = ColorUtil.darkenColor(
      ContextCompat.getColor(requireContext(), R.color.deck_blue),
      byAmount = 0.20f
    )
    activity?.window?.statusBarColor = statusbarColor
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.settings -> {
        findNavController().navigate(R.id.openSettings)
        true
      }
      R.id.viewList -> {
        viewType = CardAdapter.CardViewType.LIST
        setupLayoutManager()
        adapter.viewType = viewType
        activity?.invalidateOptionsMenu()
        true
      }
      R.id.viewGrid -> {
        viewType = CardAdapter.CardViewType.GRID
        setupLayoutManager()
        adapter.viewType = viewType
        activity?.invalidateOptionsMenu()
        true
      }
      R.id.about -> {
        AboutDialogFragment().show(parentFragmentManager, "about")
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onDestroyOptionsMenu() {
    this.optionsMenuSubscribers.clear()
    super.onDestroyOptionsMenu()
  }

  override fun onDestroyView() {
    this.subscribers.clear()
    super.onDestroyView()
  }

  private fun setupLayoutManager() {
    when (viewType) {
      CardAdapter.CardViewType.GRID -> {
        val displayMetrics = resources.displayMetrics
        val columns = (displayMetrics.widthPixels / resources.getDimension(R.dimen.card_width)).toInt()
        recyclerView.layoutManager = GridLayoutManager(requireContext(), columns)
      }
      else -> {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
      }
    }
  }

  private fun onCardSelected(cardId: String) {
    findNavController().navigate(CardListFragmentDirections.showDetail(cardId))
  }
}
