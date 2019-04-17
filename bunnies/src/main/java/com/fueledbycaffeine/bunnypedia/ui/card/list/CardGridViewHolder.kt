package com.fueledbycaffeine.bunnypedia.ui.card.list

import androidx.core.content.ContextCompat
import android.view.View
import com.bumptech.glide.RequestManager
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_view_grid_item.*

class CardGridViewHolder(override val containerView: View) : CardViewHolder(containerView), LayoutContainer {
  override fun bind(requestManager: RequestManager, cardAndRules: CardWithRules) {
    requestManager
      .load(cardAndRules.card.imageURI)
      .into(image)

    val card = cardAndRules.card
    val chipColor = ContextCompat.getColor(itemView.context, card.deck.color)
    titleBackground.setBackgroundColor(chipColor)
    title.setTextColor(ColorUtil.contrastColor(chipColor))
    title.text = card.title
  }

  override fun clear() {
    title.text = ""
    title.setTextColor(
      ContextCompat.getColor(itemView.context, R.color.white)
    )
    titleBackground.setBackgroundColor(
      ContextCompat.getColor(itemView.context, R.color.deck_blue)
    )
    image.setImageDrawable(null)
  }
}
