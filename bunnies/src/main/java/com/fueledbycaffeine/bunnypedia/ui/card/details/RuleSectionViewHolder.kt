package com.fueledbycaffeine.bunnypedia.ui.card.details

import android.net.Uri
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import com.fueledbycaffeine.bunnypedia.database.model.Rule
import com.fueledbycaffeine.bunnypedia.databinding.ListItemRuleSectionBinding
import com.fueledbycaffeine.bunnypedia.ext.android.setHtmlText
import com.fueledbycaffeine.bunnypedia.ui.EpoxyLayoutContainer
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import androidx.core.net.toUri

class RuleSectionViewHolder : EpoxyLayoutContainer() {
  lateinit var linkListener: ((Uri) -> Unit)
  private lateinit var binding: ListItemRuleSectionBinding

  override fun bindView(itemView: View) {
    super.bindView(itemView)
    binding = ListItemRuleSectionBinding.bind(itemView)
    binding.ruleText.movementMethod = LinkMovementMethod.getInstance()

    val mm = BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, binding.ruleText)
    mm.setOnLinkClickListener { _, url ->
      val uri = url.toUri()
      linkListener(uri)
      uri.scheme == "bunnypedia"
    }
  }

  fun display(rule: Rule) {
    binding.ruleTitle.text = rule.title
    binding.ruleText.setHtmlText(rule.text)
  }
}
