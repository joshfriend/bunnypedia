package com.fueledbycaffeine.bunnypedia.ui.card.details

import android.net.Uri
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.Rule

@EpoxyModelClass(layout = R.layout.list_item_rule_section)
abstract class RuleSectionModel : EpoxyModelWithHolder<RuleSectionViewHolder>() {
  @EpoxyAttribute
  lateinit var rule: Rule

  @EpoxyAttribute(DoNotHash)
  lateinit var linkListener: (Uri) -> Unit

  override fun bind(holder: RuleSectionViewHolder) {
    holder.display(rule)
    holder.linkListener = linkListener
  }
}
