package com.fueledbycaffeine.bunnypedia.ui.card

import android.support.v7.widget.RecyclerView
import android.view.View
import com.fueledbycaffeine.bunnypedia.database.Card

abstract class CardViewHolder(view: View?): RecyclerView.ViewHolder(view) {
  abstract fun bind(card: Card)
}
