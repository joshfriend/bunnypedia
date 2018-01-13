package com.fueledbycaffeine.bunnypedia.ui.card

import android.arch.lifecycle.ViewModel
import com.fueledbycaffeine.bunnypedia.database.model.Card
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class CardNavigationViewModel: ViewModel() {
  private val cardSubject = PublishSubject.create<Card>()

  fun viewCard(card: Card) {
    cardSubject.onNext(card)
  }

  fun getNavigationEvents(): Observable<Card> {
    return cardSubject.share()
  }

  override fun onCleared() {
    cardSubject.onComplete()
  }
}
