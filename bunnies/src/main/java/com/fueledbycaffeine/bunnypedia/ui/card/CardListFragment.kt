package com.fueledbycaffeine.bunnypedia.ui.card

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.paging.RxPagedListBuilder
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.Card
import com.fueledbycaffeine.bunnypedia.database.CardStore
import com.fueledbycaffeine.bunnypedia.database.QueryResult
import com.fueledbycaffeine.bunnypedia.database.model.Deck
import com.fueledbycaffeine.bunnypedia.ext.android.*
import com.fueledbycaffeine.bunnypedia.ext.rx.mapToResult
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment.Companion.ARG_CARD_ID
import com.fueledbycaffeine.bunnypedia.ui.settings.SettingsActivity
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChangeEvents
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_card_list.*
import timber.log.Timber
import javax.inject.Inject

class CardListFragment: DaggerFragment() {
  companion object {
    private const val REQ_SETTINGS = 1
  }

  @Inject lateinit var cardStore: CardStore
  private lateinit var adapter: CardAdapter
  private var optionsMenuSubscribers = CompositeDisposable()
  private var subscribers = CompositeDisposable()
  private val shownDecks = BehaviorSubject.create<Set<Deck>>()
  private val querySubject = BehaviorSubject.create<String>()

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

    shownDecks.onNext(getAvailableDecks())
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

    queryEvents.map { it.queryText().toString().trim() }
      .subscribe { querySubject.onNext(it) }
      .addTo(this.optionsMenuSubscribers)

    queryEvents.filter { it.isSubmitted }
      .map { it.queryText().toString().trim() }
      .map { it.toIntOrNull() ?: -1 }
      .observeOn(Schedulers.io())
      .flatMapSingle { cardId ->
        cardStore.getCard(cardId).mapToResult()
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onNext = { result ->
          when (result) {
            is QueryResult.Found<*> -> {
              val card = result.item as Card
              search.setQuery("", false)
              searchMenuItem.collapseActionView()
              this.onCardSelected(card.id)
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

    makeNavBarsFancy()

    // TODO: https://github.com/bumptech/glide/tree/master/integration/recyclerview
    adapter = CardAdapter(this, viewType, this::onCardSelected)
    recyclerView.adapter = adapter
    setupLayoutManager()

    fastScroller.setRecyclerView(recyclerView)

    Observables
      .combineLatest(shownDecks, querySubject)
      .map { (decks, search) -> cardStore.getCards(decks, search) }
      .flatMap { dsf -> RxPagedListBuilder(dsf, 100).buildObservable() }
      .subscribe { pl -> adapter.submitList(pl) }
      .addTo(this.subscribers)
  }

  override fun onPrepareOptionsMenu(menu: Menu) {
    val isGrid = viewType == CardAdapter.CardViewType.GRID
    menu.findItem(R.id.viewList).isVisible = isGrid
    menu.findItem(R.id.viewGrid).isVisible = !isGrid
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.settings -> {
        startActivityForResult<SettingsActivity>(REQ_SETTINGS)
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
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
      REQ_SETTINGS -> {
        shownDecks.onNext(getAvailableDecks())
        adapter.viewType = viewType
        setupLayoutManager()
        activity?.invalidateOptionsMenu()
      }
      else -> super.onActivityResult(requestCode, resultCode, data)
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

  private fun makeNavBarsFancy() {
    // It is not possible to tell which window your app occupies in multiwindow
    // mode when fitsSystemWindows is false. Apps can't draw under the navbar
    // in multiwindow mode anyways.
    val fitSystemWindows = if (activity?.isInMultiWindow == true) {
      true
    } else {
      resources.getBoolean(R.bool.fullscreen_style_fit_system_windows)
    }
    // Override the activity's theme when in multiwindow.
    coordinator.fitsSystemWindows = fitSystemWindows

    if (!fitSystemWindows) {
      // Inset bottom of content if drawing under the translucent navbar, but
      // only if the navbar is a software bar and is on the bottom of the
      // screen.
      if (resources.showsSoftwareNavBar && resources.isNavBarAtBottom) {
        recyclerView.updatePaddingRelative(
          bottom = recyclerView.paddingBottom + resources.navBarHeight
        )
      }

      // Inset the toolbar when it is drawn under the status bar.
      barLayout.updatePaddingRelative(
        top = barLayout.paddingTop + resources.statusBarHeight
      )
    }
  }

  private fun setupLayoutManager() {
    when (viewType) {
      CardAdapter.CardViewType.GRID -> {
        val displayMetrics = resources.displayMetrics
        val columns = (displayMetrics.widthPixels / resources.getDimension(R.dimen.card_width)).toInt()
        recyclerView.layoutManager = GridLayoutManager(requireContext(), columns)
      }
      CardAdapter.CardViewType.LIST -> {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
      }
    }
  }

  private fun onCardSelected(cardId: Int) {
    startActivity<CardDetailActivity>(ARG_CARD_ID to cardId)
  }

  private fun getAvailableDecks(): Set<Deck> {
    return defaultSharedPreferences.getStringSet(
      getString(R.string.pref_key_booster_decks),
      resources.getStringArray(R.array.pref_all_booster_decks_values).toSet()
    )!!.map { Deck.valueOf(it) }.toSet() + setOf(Deck.BLUE)
  }
}
