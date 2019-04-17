package com.fueledbycaffeine.bunnypedia.ui.card.details

import android.net.Uri
import com.airbnb.epoxy.EpoxyController
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import io.reactivex.subjects.PublishSubject

class CardDetailsEpoxyController(private val details: CardWithRules) : EpoxyController() {
  val links = PublishSubject.create<Uri>()

  init {
    requestModelBuild()
  }

  override fun buildModels() {
    cardSection {
      id("card")
      card(details.card)
    }
    details.rules.forEach { rule ->
      ruleSection {
        id(rule.id)
        rule(rule)
        linkListener(links::onNext)
      }
    }
  }
}
