package com.fueledbycaffeine.bunnypedia.ui.card.list

import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.Target
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_view_grid_item.image
import kotlinx.android.synthetic.main.card_view_grid_item.title
import kotlinx.android.synthetic.main.card_view_grid_item.titleBackground

class CardGridViewHolder(override val containerView: View) : CardViewHolder(containerView), LayoutContainer {
  override fun bind(requestManager: RequestManager, cardAndRules: CardWithRules) {
    requestManager
      .load(cardAndRules.card.imageURI)
      .override(Target.SIZE_ORIGINAL)
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
