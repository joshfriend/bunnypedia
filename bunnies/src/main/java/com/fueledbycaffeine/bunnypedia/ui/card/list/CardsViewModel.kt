@file:Suppress("HasPlatformType")

package com.fueledbycaffeine.bunnypedia.ui.card.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.CardStore
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.database.model.Deck
import com.fueledbycaffeine.bunnypedia.ext.android.defaultSharedPreferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CardsViewModel(
  private val context: Context,
  private val cardStore: CardStore
) : ViewModel() {
  private val shownDecks = BehaviorSubject.create<Set<Deck>>()
  private val querySubject = BehaviorSubject.create<String>()
  private val cardListSubject = BehaviorSubject.create<LiveData<PagedList<CardWithRules>>>()
  private val disposables = CompositeDisposable()

  init {
    reloadAvailableDecks()

    Observables
      .combineLatest(
        shownDecks.distinctUntilChanged(),
        querySubject.distinctUntilChanged()
      )
      .map { (decks, search) -> cardStore.getCards(decks, search) }
      .map { dsf -> LivePagedListBuilder(dsf, 100).build() }
      .subscribe(cardListSubject)
  }

  override fun onCleared() {
    this.disposables.clear()
    super.onCleared()
  }

  fun setQuery(query: String) {
    querySubject.onNext(query)
  }

  fun reloadAvailableDecks() {
    val decks = getAvailableDecks()
    shownDecks.onNext(decks)
  }

  fun observe() = cardListSubject.share()

  private fun getAvailableDecks(): Set<Deck> {
    val decks = context.defaultSharedPreferences.getStringSet(
      context.getString(R.string.pref_key_booster_decks),
      null
    ) ?: context.resources.getStringArray(R.array.pref_all_booster_decks_values).toSet()
    return decks.map { Deck.valueOf(it) }.toSet() + setOf(Deck.BLUE)
  }

  //<editor-fold desc="Factory">
  @Suppress("UNCHECKED_CAST")
  class Provider @Inject constructor(
    private val context: Context,
    private val cardStore: CardStore
  ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
        return CardsViewModel(context, cardStore) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class!")
    }
  }
  //</editor-fold>
}
