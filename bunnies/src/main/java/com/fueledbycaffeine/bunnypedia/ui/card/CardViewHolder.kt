package com.fueledbycaffeine.bunnypedia.ui.card

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fueledbycaffeine.bunnypedia.database.model.Card

abstract class CardViewHolder(view: View): RecyclerView.ViewHolder(view) {
  abstract fun bind(card: Card)
  abstract fun clear()
}
