package com.fueledbycaffeine.bunnypedia.ui.card

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class CardNavigationViewModel: ViewModel() {
  private val cardSubject = PublishSubject.create<Int>()

  fun viewCard(card: Int) {
    cardSubject.onNext(card)
  }

  fun getNavigationEvents(): Observable<Int> {
    return cardSubject.share()
  }

  override fun onCleared() {
    cardSubject.onComplete()
  }
}
