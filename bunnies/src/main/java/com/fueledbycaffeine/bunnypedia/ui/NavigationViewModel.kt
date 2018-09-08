package com.fueledbycaffeine.bunnypedia.ui

import androidx.lifecycle.ViewModel
import com.fueledbycaffeine.bunnypedia.database.model.Card
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class NavigationViewModel: ViewModel() {
  sealed class Action {
    object ShowList: Action()
    data class ShowCard(val card: Card): Action()
    object GoBack: Action()
  }

  private val subject = PublishSubject.create<Action>()

  private fun performAction(action: Action) {
    Timber.d("Navigation action: $action")
    subject.onNext(action)
  }

  fun showCard(card: Card) {
    performAction(Action.ShowCard(card))
  }

  fun showList() {
    performAction(Action.ShowList)
  }

  fun goBack() {
    performAction(Action.GoBack)
  }

  val actions: Observable<Action> get() = subject.share()
}
