package com.fueledbycaffeine.bunnypedia.ui.card

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import com.fueledbycaffeine.bunnypedia.database.RulesSection
import com.fueledbycaffeine.bunnypedia.ext.android.setHtmlText
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_rule_section.*

class RuleSectionViewHolder(override val containerView: View?): RecyclerView.ViewHolder(containerView), LayoutContainer {

  init {
    ruleText.movementMethod = LinkMovementMethod.getInstance()
  }

  fun bind(rule: RulesSection) {
    ruleTitle.text = rule.title
    ruleText.setHtmlText(rule.text)
  }
}
