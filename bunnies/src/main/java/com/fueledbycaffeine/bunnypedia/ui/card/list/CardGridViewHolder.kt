package com.fueledbycaffeine.bunnypedia.ui.card.list

import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.Target
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.databinding.CardViewGridItemBinding
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import kotlinx.android.extensions.LayoutContainer

class CardGridViewHolder(override val containerView: View) : CardViewHolder(containerView), LayoutContainer {
  private val binding = CardViewGridItemBinding.bind(containerView)

  override fun bind(requestManager: RequestManager, cardAndRules: CardWithRules) {
    requestManager
      .load(cardAndRules.card.imageURI)
      .override(Target.SIZE_ORIGINAL)
      .into(binding.image)

    val card = cardAndRules.card
    val chipColor = ContextCompat.getColor(itemView.context, card.deck.color)
    binding.titleBackground.setBackgroundColor(chipColor)
    binding.title.setTextColor(ColorUtil.contrastColor(chipColor))
    binding.title.text = card.title
  }

  override fun clear() {
    binding.title.text = ""
    binding.title.setTextColor(
      ContextCompat.getColor(itemView.context, R.color.white)
    )
    binding.titleBackground.setBackgroundColor(
      ContextCompat.getColor(itemView.context, R.color.deck_blue)
    )
    binding.image.setImageDrawable(null)
  }
}
