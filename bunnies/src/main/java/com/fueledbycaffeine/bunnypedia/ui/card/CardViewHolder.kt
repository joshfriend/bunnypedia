package com.fueledbycaffeine.bunnypedia.ui.card

import android.support.v7.widget.RecyclerView
import android.view.View
import com.fueledbycaffeine.bunnypedia.database.Card
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_card.*

class CardViewHolder(override val containerView: View?): RecyclerView.ViewHolder(containerView), LayoutContainer {

  fun bind(card: Card) {
    val chipColor = card.deck.getColor(itemView.context)
    cardNumber.setBackgroundColor(chipColor)
    cardNumber.setTextColor(ColorUtil.contrastColor(chipColor))
    cardNumber.text = String.format("#%04d", card.id)

    title.text = card.title

    cardType.text = card.type.localizedDescription(itemView.context)
  }
}

