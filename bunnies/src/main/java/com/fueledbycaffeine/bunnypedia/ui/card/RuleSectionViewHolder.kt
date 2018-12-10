package com.fueledbycaffeine.bunnypedia.ui.card

import android.graphics.Color
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.Rule
import com.fueledbycaffeine.bunnypedia.database.model.RuleType
import com.fueledbycaffeine.bunnypedia.ext.android.getString
import com.fueledbycaffeine.bunnypedia.ext.android.setHtmlText
import com.fueledbycaffeine.bunnypedia.util.RoundedBackgroundSpan
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_rule_section.*
import org.jetbrains.anko.buildSpanned

class RuleSectionViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {

  init {
    ruleText.movementMethod = LinkMovementMethod.getInstance()
  }

  fun bind(rule: Rule) {
    ruleTitle.text = buildSpanned {
      append(rule.title)

      val typeText = when (val res = rule.type.text) {
        -1 -> null
        else -> getString(res)
      }

      if (typeText != null) {
        append("      ")
        append(typeText)

        val chipBg = RoundedBackgroundSpan(
          containerView.context.getColor(rule.type.color),
          Color.WHITE
        )
        val start = rule.title.length + 6
        setSpan(chipBg, start, start + typeText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
      }
    }
    ruleText.setHtmlText(rule.text)
  }
}
