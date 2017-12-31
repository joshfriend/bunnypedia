package com.fueledbycaffeine.bunnypedia.ui.card

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.Card

class CardAdapter(
  private val allCards: List<Card>,
  private val onCardSelected: (Card) -> Unit
): RecyclerView.Adapter<CardViewHolder>() {

  private var _shownCards = allCards
  private var shownCards: List<Card>
    get() = _shownCards
    set(value) {
      _shownCards = value
      notifyDataSetChanged()
    }

  override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
    val card = shownCards[holder.adapterPosition]
    holder.bind(card)
    holder.itemView.setOnClickListener { onCardSelected(card) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
    val inflater = LayoutInflater.from(parent.context)

    val view = inflater.inflate(R.layout.list_item_card, parent, false)

    return CardViewHolder(view)
  }

  override fun getItemCount(): Int {
    return shownCards.size
  }

  fun setQuery(query: String?) {
    shownCards = when (query) {
      null -> allCards
      else -> allCards.filter { it.title.contains(query, ignoreCase = true) }
    }
  }
}
