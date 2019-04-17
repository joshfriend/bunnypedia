package com.fueledbycaffeine.bunnypedia.ui.card.details

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.Card

@EpoxyModelClass(layout = R.layout.card_hero_details)
abstract class CardSectionModel : EpoxyModelWithHolder<CardSectionViewHolder>() {
  @EpoxyAttribute
  lateinit var card: Card

  override fun bind(holder: CardSectionViewHolder) {
    super.bind(holder)
    holder.display(card)
  }
}
