package com.fueledbycaffeine.bunnypedia.ui.card

import android.arch.paging.PagedListAdapter
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.Card
import com.fueledbycaffeine.bunnypedia.ui.GlideApp
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider
import org.jetbrains.anko.layoutInflater

class CardAdapter(
  val fragment: Fragment,
  viewType: CardViewType,
  private val onCardSelected: (Int) -> Unit
): PagedListAdapter<Card, CardViewHolder>(CARD_COMPARATOR), SectionTitleProvider {

  companion object {
    private val CARD_COMPARATOR = object : DiffUtil.ItemCallback<Card>() {
      override fun areItemsTheSame(oldItem: Card, newItem: Card) = oldItem.id == newItem.id
      override fun areContentsTheSame(oldItem: Card, newItem: Card) = oldItem == newItem
    }
  }

  enum class CardViewType {
    LIST,
    GRID,
    ;
  }

  var viewType: CardViewType = viewType
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun getItemViewType(position: Int): Int {
    return viewType.ordinal
  }

  override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
    val card = getItem(holder.adapterPosition)
    if (card == null) {
      holder.clear()
    } else {
      when (holder) {
        is CardGridViewHolder -> holder.bind(GlideApp.with(fragment), card)
        else -> holder.bind(card)
      }
      holder.itemView.setOnClickListener { onCardSelected(card.id) }
    }
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

  override fun getSectionTitle(position: Int): String {
    val card = getItem(position) ?: return "???"
    return card.id.toString()
  }
}
