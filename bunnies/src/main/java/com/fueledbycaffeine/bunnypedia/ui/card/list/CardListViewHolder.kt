package com.fueledbycaffeine.bunnypedia.ui.card.list

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.Target
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.databinding.CardViewListItemBinding
import com.fueledbycaffeine.bunnypedia.ext.android.stripHtmlTags
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import kotlinx.android.extensions.LayoutContainer
import java.util.Locale

class CardListViewHolder(override val containerView: View) : CardViewHolder(containerView), LayoutContainer {
  private val binding = CardViewListItemBinding.bind(containerView)

  override fun bind(requestManager: RequestManager, cardAndRules: CardWithRules) {
    requestManager
      .load(cardAndRules.card.imageURI)
      .override(Target.SIZE_ORIGINAL)
      .into(binding.image)

    val (card, rules) = cardAndRules
    binding.cardNumber.text = String.format(Locale.US, "#%s", card.id)
    binding.title.text = card.title
    binding.cardText.text = rules.firstOrNull()?.text?.stripHtmlTags() ?: ""

    val chipColor = ContextCompat.getColor(itemView.context, card.deck.color)
    binding.cardNumber.backgroundTintList = ColorStateList.valueOf(chipColor)
    binding.cardNumber.setTextColor(ColorUtil.contrastColor(chipColor))
  }

  override fun clear() {
    binding.title.text = ""
    binding.cardNumber.text = "#???"
    binding.cardText.text = ""
    binding.cardNumber.setTextColor(
      ContextCompat.getColor(itemView.context, R.color.white)
    )
    binding.cardNumber.backgroundTintList = ColorStateList.valueOf(
      ContextCompat.getColor(itemView.context, R.color.deck_blue)
    )
  }
}
