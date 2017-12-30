package com.fueledbycaffeine.bunnypedia.ui.card

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.Card
import com.fueledbycaffeine.bunnypedia.database.Die
import com.fueledbycaffeine.bunnypedia.database.ZodiacType
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import com.google.android.flexbox.FlexboxLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_hero_details.*
import kotlinx.android.synthetic.main.content_card_detail.*
import kotlinx.android.synthetic.main.fragment_card_detail.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber

class CardDetailFragment: Fragment() {
  companion object {
    const val ARG_CARD = "CARD"

    private val ZODIAC_DATE_FMT = DateTimeFormat.forPattern("MMMM d")
  }

  private val cardArgument by lazy {
    arguments!!.getParcelable(ARG_CARD) as Card
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_card_detail, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val activity = activity ?: return
    val card = cardArgument

    toolbar.title = card.title
    toolbar.subtitle = "${String.format("#%04d", card.id)} – ${card.deck.localizedDescription(activity)}"
    toolbar.setNavigationOnClickListener { activity.finish() }

    val deckColor = card.deck.getColor(context!!)
    toolbar.setBackgroundColor(deckColor)
    val titleColor = ColorUtil.contrastColor(deckColor)
    toolbar.setTitleTextColor(titleColor)
    toolbar.setSubtitleTextColor(titleColor)
    val statusbarColor = ColorUtil.darkenColor(deckColor, byAmount = 0.20f)
    activity.window?.statusBarColor = statusbarColor
    toolbar.navigationIcon?.setTint(titleColor)

    if (ColorUtil.getLuminance(statusbarColor) < 0.5) {
      var flags = view.systemUiVisibility
      flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
      view.systemUiVisibility = flags
    }

    if (card.description.isNotEmpty()) {
      containerDescription.visibility = View.VISIBLE
      description.text = card.description
    }

    cardType.text = card.type.localizedDescription(activity)

    Picasso.get()
      .load(card.imageURI)
      .fit()
      .into(cardThumbnail, object: Callback {
        override fun onSuccess() {}

        override fun onError(e: Exception) {
          Timber.e(e, "Unable to load image")
        }
      })

    if (card.dice.isNotEmpty()) {
      setupDiceInfo(card.dice)
    }

    if (card.weaponLevel != null) {
      weaponLevelContainer.visibility = View.VISIBLE
      weaponLevel.text = card.weaponLevel
    }

    if (card.requiresBunny != null) {
      containerRequiresBunny.visibility = View.VISIBLE
      requiresBunny.text = when (card.requiresBunny) {
        true -> getString(R.string.yes)
        false -> getString(R.string.no)
      }
    }

    if (card.isFtb) {
      setupFtbInfo(card.cabbage, card.water)
    }
  }

  override fun onResume() {
    super.onResume()
    val card = cardArgument
    if (card.zodiacType != null) {
      setupZodiacInfo(card.zodiacType)
    }
  }

  private fun setupDiceInfo(dice: List<Die>) {
    diceContainer.visibility = View.VISIBLE
    for (die in dice) {
      val img = ImageView(activity)
      img.setImageDrawable(die.getDrawable(activity!!))
      // TODO: WTF
      img.layoutParams = FlexboxLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
      )
      diceContainer.addView(img)
    }
  }

  private fun setupFtbInfo(cabbage: Int, water: Int) {
    containerFtb.visibility = View.VISIBLE
    if (cabbage > 0 && water > 0) {
      ftbRequirement.text = getString(R.string.ftb_cabbage_water, cabbage, water)
    } else if (cabbage > 0) {
      ftbRequirement.text = getString(R.string.ftb_cabbage, cabbage)
    } else if (water > 0) {
      ftbRequirement.text = getString(R.string.ftb_water, water)
    } else if (cabbage == Card.FTB_RANDOM) {
      ftbRequirement.text = getString(R.string.ftb_random)
    } else if (cabbage == Card.FTB_DATED) {
      ftbRequirement.text = getString(R.string.ftb_dated_cabbage)
    } else if (water == Card.FTB_DATED) {
      ftbRequirement.text = getString(R.string.ftb_dated_water)
    }
  }

  private fun setupZodiacInfo(zodiac: ZodiacType) {
    containerZodiacSign.visibility = View.VISIBLE
    containerZodiacDate.visibility = View.VISIBLE

    zodiacSymbol.text = getString(
      R.string.zodiac_info,
      getString(zodiac.description),
      getString(zodiac.element.description)
    )
    zodiacSymbol.setCompoundDrawablesWithIntrinsicBounds(zodiac.symbol, 0, 0, 0)
    zodiacSymbol.compoundDrawableTintList = ColorStateList.valueOf(
      ContextCompat.getColor(context!!, zodiac.element.tintColor)
    )

    val (startMonthDay, endMonthDay) = zodiac.range
    val start = DateTime()
      .withTimeAtStartOfDay()
      .withDayOfMonth(startMonthDay.dayOfMonth)
      .withMonthOfYear(startMonthDay.monthOfYear)
    // This works because zodiac dates never end at the end of a month
    val end = start.plusMonths(1)
      .withDayOfMonth(endMonthDay.dayOfMonth)
      .plusDays(1)  // Non-inclusive!
    val now = DateTime()

    if (start <= now && end > now) {
      zodiacDate.text = getString(
        R.string.zodiac_date_range_current,
        ZODIAC_DATE_FMT.print(start),
        ZODIAC_DATE_FMT.print(end.minusDays(1))
      )
    } else {
      zodiacDate.text = getString(
        R.string.zodiac_date_range,
        ZODIAC_DATE_FMT.print(start),
        ZODIAC_DATE_FMT.print(end.minusDays(1))
      )
    }
  }
}
