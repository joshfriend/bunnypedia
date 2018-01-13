package com.fueledbycaffeine.bunnypedia.ui.card

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.Card
import com.fueledbycaffeine.bunnypedia.database.model.Deck
import com.fueledbycaffeine.bunnypedia.ui.GlideApp
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider
import org.jetbrains.anko.layoutInflater
import timber.log.Timber

class CardAdapter(
  val fragment: Fragment,
  viewType: CardViewType,
  private var allCards: List<Card>,
  private val onCardSelected: (Card) -> Unit
): RecyclerView.Adapter<CardViewHolder>(), SectionTitleProvider {

  enum class CardViewType {
    LIST,
    GRID,
    ;
  }

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
  var viewType: CardViewType = viewType
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  private var shownCards: List<Card> = allCards
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun getItemViewType(position: Int): Int {
    return viewType.ordinal
  }

  override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
    val card = shownCards[holder.adapterPosition]
    when (holder) {
      is CardGridViewHolder -> holder.bind(GlideApp.with(fragment), card)
      else -> holder.bind(card)
    }
    holder.itemView.setOnClickListener { onCardSelected(card) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
    val type = CardViewType.values()[viewType]
    val inflater = parent.context.layoutInflater
    return when (type) {
      CardViewType.LIST -> {
        val view = inflater.inflate(R.layout.card_view_list_item, parent, false)
        CardListViewHolder(view)
      }
      CardViewType.GRID -> {
        val view = inflater.inflate(R.layout.card_view_grid_item, parent, false)
        CardGridViewHolder(view)
      }
    }
  }

  override fun getItemCount(): Int {
    return shownCards.size
  }

  override fun getSectionTitle(position: Int): String {
    val card = allCards[position]
    return card.id.toString()
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
