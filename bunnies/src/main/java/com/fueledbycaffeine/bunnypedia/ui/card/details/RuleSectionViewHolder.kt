package com.fueledbycaffeine.bunnypedia.ui.card.details

import android.net.Uri
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import com.fueledbycaffeine.bunnypedia.database.model.Rule
import com.fueledbycaffeine.bunnypedia.ext.android.setHtmlText
import com.fueledbycaffeine.bunnypedia.ui.EpoxyLayoutContainer
import kotlinx.android.synthetic.main.list_item_rule_section.view.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod

class RuleSectionViewHolder : EpoxyLayoutContainer() {
  lateinit var linkListener: ((Uri) -> Unit)

  override fun bindView(itemView: View) {
    super.bindView(itemView)
    itemView.ruleText.movementMethod = LinkMovementMethod.getInstance()

    val mm = BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, itemView.ruleText)
    mm.setOnLinkClickListener { _, url ->
      val uri = Uri.parse(url)
      linkListener(uri)
      uri.scheme == "bunnypedia"
    }
  }

  fun display(rule: Rule) {
    itemView.ruleTitle.text = rule.title
    itemView.ruleText.setHtmlText(rule.text)
  }
}
