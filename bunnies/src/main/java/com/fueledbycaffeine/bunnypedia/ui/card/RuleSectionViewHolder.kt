package com.fueledbycaffeine.bunnypedia.ui.card

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fueledbycaffeine.bunnypedia.database.model.Rule
import com.fueledbycaffeine.bunnypedia.ext.android.setHtmlText
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_rule_section.*

class RuleSectionViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {

  init {
    ruleText.movementMethod = LinkMovementMethod.getInstance()
  }

  fun bind(rule: Rule) {
    ruleTitle.text = rule.title
    ruleText.setHtmlText(rule.text)
  }
}
