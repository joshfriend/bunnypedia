package com.fueledbycaffeine.bunnypedia.ui.card.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules

abstract class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  abstract fun bind(requestManager: RequestManager, cardAndRules: CardWithRules)
  abstract fun clear()
}
