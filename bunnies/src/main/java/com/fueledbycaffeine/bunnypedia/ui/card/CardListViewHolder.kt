package com.fueledbycaffeine.bunnypedia.ui.card

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.RequestManager
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.ext.android.stripHtmlTags
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_view_list_item.*

class CardListViewHolder(override val containerView: View) : CardViewHolder(containerView), LayoutContainer {
  override fun bind(requestManager: RequestManager, cardAndRules: CardWithRules) {
    requestManager
      .load(cardAndRules.card.imageURI)
      .into(image)

    val (card, rules) = cardAndRules
    cardNumber.text = String.format("#%04d", card.id)
    title.text = card.title
    cardText.text = rules.firstOrNull()?.text?.stripHtmlTags() ?: ""

    val chipColor = ContextCompat.getColor(itemView.context, card.deck.color)
    cardNumber.backgroundTintList = ColorStateList.valueOf(chipColor)
    cardNumber.setTextColor(ColorUtil.contrastColor(chipColor))
  }

  override fun clear() {
    title.text = ""
    cardNumber.text = "#???"
    cardText.text = ""
    cardNumber.setTextColor(
      ContextCompat.getColor(itemView.context, R.color.white)
    )
    cardNumber.backgroundTintList = ColorStateList.valueOf(
      ContextCompat.getColor(itemView.context, R.color.deck_blue)
    )
  }
}
