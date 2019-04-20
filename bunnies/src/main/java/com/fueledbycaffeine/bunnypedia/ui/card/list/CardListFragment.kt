package com.fueledbycaffeine.bunnypedia.ui.card.list

import android.content.Context
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
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.CardStore
import com.fueledbycaffeine.bunnypedia.database.QueryResult
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.ext.android.defaultSharedPreferences
import com.fueledbycaffeine.bunnypedia.ext.android.hideSoftKeyboard
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
  private lateinit var cardsViewModel: CardsViewModel

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

  override fun onAttach(context: Context) {
    super.onAttach(context)

    cardsViewModel = ViewModelProviders.of(this, cardsViewModelProvider)
      .get(CardsViewModel::class.java)
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
      .map { it.toIntOrNull() ?: -1 }
      .observeOn(Schedulers.io())
      .flatMapSingle { cardId ->
        cardStore.getCard(cardId).mapToResult()
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onNext = { result ->
          hideSoftKeyboard()
          when (result) {
            is QueryResult.Found<*> -> {
              val item = result.item as CardWithRules
              search.setQuery("", false)
              searchMenuItem.collapseActionView()
              this.onCardSelected(item.card.id)
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
        data.observe(this, adapter::submitList)
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
        AboutDialogFragment().show(requireFragmentManager(), "about")
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

  private fun onCardSelected(cardId: Int) {
    findNavController().navigate(CardListFragmentDirections.showDetail(cardId))
  }
}
