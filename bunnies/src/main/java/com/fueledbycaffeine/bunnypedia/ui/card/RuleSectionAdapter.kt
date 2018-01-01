@file:Suppress("HasPlatformType")

package com.fueledbycaffeine.bunnypedia.ui.card

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.util.Linkify
import android.view.ViewGroup
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.RulesSection
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.list_item_rule_section.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import org.jetbrains.anko.layoutInflater

class RuleSectionAdapter(val rules: List<RulesSection>): RecyclerView.Adapter<RuleSectionViewHolder>() {
  val linksClicked = PublishSubject.create<Uri>()

  override fun getItemCount() = rules.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuleSectionViewHolder {
    val inflater = parent.context.layoutInflater
    val view = inflater.inflate(R.layout.list_item_rule_section, parent, false)
    val holder =  RuleSectionViewHolder(view)

    val mm = BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, holder.ruleText)
    mm.setOnLinkClickListener { _, url ->
      val uri = Uri.parse(url)
      linksClicked.onNext(uri)
      uri.scheme == "bunnypedia"
    }

    return holder
  }

  override fun onBindViewHolder(holder: RuleSectionViewHolder, position: Int) {
    val rule = rules[holder.adapterPosition]
    holder.bind(rule)
  }
}
