package com.fueledbycaffeine.bunnypedia.ui.card

import android.support.v4.content.ContextCompat
import android.view.View
import com.bumptech.glide.RequestManager
import com.fueledbycaffeine.bunnypedia.database.Card
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_view_grid_item.*

class CardGridViewHolder(override val containerView: View?): CardViewHolder(containerView), LayoutContainer {
  fun bind(requestManager: RequestManager, card: Card) {
    this.bind(card)

    requestManager
      .load(card.imageURI)
      .into(image)
  }

  override fun bind(card: Card) {
    val chipColor = ContextCompat.getColor(itemView.context, card.deck.color)
    title.setBackgroundColor(chipColor)
    title.setTextColor(ColorUtil.contrastColor(chipColor))
    title.text = card.title
  }
}
