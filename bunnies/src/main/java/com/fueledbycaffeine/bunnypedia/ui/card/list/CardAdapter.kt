package com.fueledbycaffeine.bunnypedia.ui.card.list

import androidx.paging.PagedListAdapter
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.ui.GlideApp
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider
import org.jetbrains.anko.layoutInflater

class CardAdapter(
  val fragment: Fragment,
  viewType: CardViewType,
  private val onCardSelected: (Int) -> Unit
) : PagedListAdapter<CardWithRules, CardViewHolder>(CARD_COMPARATOR), SectionTitleProvider {

  companion object {
    private val CARD_COMPARATOR = object : DiffUtil.ItemCallback<CardWithRules>() {
      override fun areItemsTheSame(oldItem: CardWithRules, newItem: CardWithRules) = oldItem.card.id == newItem.card.id
      override fun areContentsTheSame(oldItem: CardWithRules, newItem: CardWithRules) = oldItem == newItem
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
    val cardWithRules = getItem(holder.adapterPosition)
    if (cardWithRules == null) {
      holder.clear()
    } else {
      holder.bind(GlideApp.with(fragment), cardWithRules)
      holder.itemView.setOnClickListener { onCardSelected(cardWithRules.card.id) }
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
    val (card) = getItem(position) ?: return "???"
    return card.id.toString()
  }
}
