package com.fueledbycaffeine.bunnypedia.ui.card

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.Card
import com.fueledbycaffeine.bunnypedia.database.Deck
import org.jetbrains.anko.layoutInflater

class CardAdapter(
  private var allCards: List<Card>,
  private val onCardSelected: (Card) -> Unit
): RecyclerView.Adapter<CardViewHolder>() {

  var shownDecks: Set<Deck> = Deck.values().toSet()
    set(value) {
      field = value
      computeShownCards()
    }
  var query: String? = null
    set(value) {
      field = value
      computeShownCards()
    }

  private var shownCards: List<Card> = allCards
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
    val card = shownCards[holder.adapterPosition]
    holder.bind(card)
    holder.itemView.setOnClickListener { onCardSelected(card) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
    val inflater = parent.context.layoutInflater
    val view = inflater.inflate(R.layout.list_item_card, parent, false)
    return CardViewHolder(view)
  }

  override fun getItemCount(): Int {
    return shownCards.size
  }

  private fun computeShownCards() {
    val availableCards = allCards.filter { shownDecks.contains(it.deck) }
    val query = query
    val cardsMatchingQuery = when (query) {
      null -> availableCards
      else -> {
        val idQuery = query.replace("^0+".toRegex(), "")
        availableCards.filter { card ->
          val titleMatch = card.title.contains(query, ignoreCase = true)
          val idMatch = card.id.toString().startsWith(idQuery)
          titleMatch || idMatch
        }
      }
    }

    shownCards = cardsMatchingQuery
  }
}
