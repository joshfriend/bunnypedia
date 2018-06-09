package com.fueledbycaffeine.bunnypedia.ui.card

import android.support.v4.content.ContextCompat
import android.view.View
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.Card
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_view_list_item.*

class CardListViewHolder(override val containerView: View?): CardViewHolder(containerView), LayoutContainer {

  override fun bind(card: Card) {
    val chipColor = ContextCompat.getColor(itemView.context, card.deck.color)
    cardNumber.setBackgroundColor(chipColor)
    cardNumber.setTextColor(ColorUtil.contrastColor(chipColor))
    cardNumber.text = String.format("#%04d", card.id)

    title.text = card.title

    cardType.text = itemView.context.getString(card.type.description)
  }

  override fun clear() {
    cardType.text = ""
    title.text = ""
    cardNumber.text = "#???"
    cardNumber.setBackgroundColor(
      ContextCompat.getColor(itemView.context, R.color.deck_blue)
    )
  }
}

