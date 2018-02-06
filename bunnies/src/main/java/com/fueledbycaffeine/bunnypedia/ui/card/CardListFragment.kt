package com.fueledbycaffeine.bunnypedia.ui.card

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.RelativeLayout
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.Card
import com.fueledbycaffeine.bunnypedia.database.Database
import com.fueledbycaffeine.bunnypedia.database.model.Deck
import com.fueledbycaffeine.bunnypedia.ext.android.hasNavBar
import com.fueledbycaffeine.bunnypedia.ext.android.isTablet
import com.fueledbycaffeine.bunnypedia.ext.android.navBarHeight
import com.fueledbycaffeine.bunnypedia.ext.android.statusBarHeight
import com.fueledbycaffeine.bunnypedia.injection.App
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment.Companion.ARG_CARD
import com.fueledbycaffeine.bunnypedia.ui.settings.SettingsActivity
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_card_list.*
import org.jetbrains.anko.alignParentEnd
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
  private var subscribers = CompositeDisposable()

  private var viewType: CardAdapter.CardViewType
    get() = CardAdapter.CardViewType.valueOf(
      defaultSharedPreferences.getString(
        getString(R.string.pref_key_view_type),
        getString(R.string.pref_default_view_type)
      )
    )
    set(value) {
      defaultSharedPreferences.edit()
        .putString(getString(R.string.pref_key_view_type), value.name)
        .apply()
    }

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
      .subscribeBy(onNext = {
        Timber.i("Search query changed: $it")
        adapter.query = it
      })
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

    makeSystemBarsFancy()

    // TODO: https://github.com/bumptech/glide/tree/master/integration/recyclerview
    adapter = CardAdapter(this, viewType, emptyList(), this::onCardSelected)
    recyclerView.adapter = adapter
    setupLayoutManager()

    fastScroller.setRecyclerView(recyclerView)

    database.getAllCards()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { cards ->
        adapter = CardAdapter(this, viewType, cards, this::onCardSelected)
        adapter.shownDecks = getAvailableDecks()
        loadingView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        recyclerView.adapter = adapter
        fastScroller.setRecyclerView(recyclerView)
        Timber.w("Cards loaded")
      }
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
        adapter.shownDecks = getAvailableDecks()
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

  private fun makeSystemBarsFancy() {
    val isMultiWindow = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      activity?.isInMultiWindowMode == true
    } else {
      false
    }

    // It is not possible to tell which window your app occupies in multiwindow
    // mode when fitsSystemWindows is false. Apps can't draw under the navbar
    // in multiwindow mode anyways.
    val fitSystemWindows = if (isMultiWindow) {
      true
    } else {
      resources.getBoolean(R.bool.fullscreen_style_fit_system_windows)
    }
    // Override the activity's theme when in multiwindow.
    coordinator.fitsSystemWindows = fitSystemWindows

    // Navbar rotates with device if its category is sw600dp or above
    val navBarAtBottom = resources.isTablet || resources.configuration.orientation == ORIENTATION_PORTRAIT

    if (!fitSystemWindows) {
      // Inset bottom of content if drawing under the translucent navbar, but
      // only if the navbar is a software bar and is on the bottom of the
      // screen.
      if (resources.hasNavBar && navBarAtBottom) {
        recyclerView.setPadding(
          recyclerView.paddingLeft,
          recyclerView.paddingTop,
          recyclerView.paddingRight,
          recyclerView.paddingBottom + resources.navBarHeight
        )
      }

      // Inset the toolbar when it is drawn under the statusbar.
      barLayout.setPadding(
        barLayout.paddingLeft,
        barLayout.paddingTop + resources.statusBarHeight,
        barLayout.paddingRight,
        barLayout.paddingBottom
      )
    }
  }

  private fun setupLayoutManager() {
    when (viewType) {
      CardAdapter.CardViewType.GRID -> {
        val displayMetrics = resources.displayMetrics
        val columns = (displayMetrics.widthPixels / resources.getDimension(R.dimen.card_width)).toInt()
        recyclerView.layoutManager = GridLayoutManager(context!!, columns)
      }
      CardAdapter.CardViewType.LIST -> {
        recyclerView.layoutManager = LinearLayoutManager(context!!)
      }
    }
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
