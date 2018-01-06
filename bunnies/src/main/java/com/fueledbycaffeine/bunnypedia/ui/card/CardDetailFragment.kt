package com.fueledbycaffeine.bunnypedia.ui.card

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.FOCUS_UP
import android.view.ViewGroup
import android.widget.ImageView
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.*
import com.fueledbycaffeine.bunnypedia.injection.App
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import com.google.android.flexbox.FlexboxLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.card_hero_details.*
import kotlinx.android.synthetic.main.content_card_detail.*
import kotlinx.android.synthetic.main.fragment_card_detail.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import javax.inject.Inject

class CardDetailFragment: Fragment() {
  companion object {
    const val ARG_CARD = "CARD"

    private val ZODIAC_DATE_FMT = DateTimeFormat.forPattern("MMMM d")
  }

  private val cardArgument by lazy {
    arguments!!.getParcelable(ARG_CARD) as Card
  }

  @Inject lateinit var database: Database
  private lateinit var navigation: CardNavigationViewModel
  private val subscribers = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("onCreate() ${cardArgument.title}")
    App.graph.inject(this)
    navigation = ViewModelProviders.of(activity!!).get(CardNavigationViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.d("onCreateView() ${cardArgument.title}")
    return inflater.inflate(R.layout.fragment_card_detail, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Timber.d("onViewCreated() ${cardArgument.title}")

    val activity = activity ?: return
    val card = cardArgument

    // It likes to scroll to the bottom by default for some reason...
    scrollView.fullScroll(FOCUS_UP)

    toolbar.title = card.title
    toolbar.subtitle = "${String.format("#%04d", card.id)} – ${getString(card.deck.description)}"
    if (activity is AppCompatActivity) {
      activity.setSupportActionBar(toolbar)
      activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    val deckColor = ContextCompat.getColor(activity, card.deck.color)
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

    cardType.text = getString(card.type.description)

    val adapter = RuleSectionAdapter(card.rules)
    adapter.linksClicked
      .subscribe { uri ->
        try {
          val cardId = uri.pathSegments[0].toInt()
          val selectedCard = database.getCard(cardId)
          if (selectedCard != null) {
            navigation.viewCard(selectedCard)
          }
        } catch (e: Exception) {
          Timber.e(e)
        }
      }
      .addTo(subscribers)
    recyclerView.adapter = adapter

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

    if (card.pawn != null) {
      setupPawnInfo(card.pawn)
    }

    if (card.weaponLevel != null) {
      weaponLevelContainer.visibility = View.VISIBLE
      weaponLevel.text = card.weaponLevel
    }

    if (card.bunnyRequirement != BunnyRequirement.NOT_APPLICABLE) {
      containerRequiresBunny.visibility = View.VISIBLE
      requiresBunny.text = getString(card.bunnyRequirement.description)
    }

    if (card.isFtb) {
      setupFtbInfo(card.cabbage, card.water)
    }

    if (card.rank != null) {
      setupRankInfo(card.rank)
    }

    if (card.psi != null) {
      setupPsiInfo(card.psi)
    }

    if (card.specialSeries != null) {
      setupSpecialSeriesInfo(card.specialSeries)
    }
  }

  override fun onResume() {
    super.onResume()
    Timber.d("onResume() ${cardArgument.title}")
    val card = cardArgument
    if (card.zodiacType != null) {
      setupZodiacInfo(card.zodiacType)
    }
  }

  override fun onDestroyView() {
    Timber.d("onDestroyView() ${cardArgument.title}")
    subscribers.clear()
    super.onDestroyView()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    Timber.d("onAttach() ${cardArgument.title}")
  }

  override fun onDetach() {
    super.onDetach()
    Timber.d("onDetach() ${cardArgument.title}")
  }

  override fun onPause() {
    super.onPause()
    Timber.d("onPause() ${cardArgument.title}")
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    Timber.d("onSaveInstanceState() ${cardArgument.title}")
  }

  private fun setupDiceInfo(dice: List<Die>) {
    diceContainer.visibility = View.VISIBLE
    for (die in dice) {
      val img = ImageView(context)
      img.setImageDrawable(die.getDrawable(context!!))
      img.layoutParams = FlexboxLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
      )
      diceFlexLayout.addView(img)
    }
  }

  private fun setupPawnInfo(pawn: Pawn) {
    containerPawnInfo.visibility = View.VISIBLE

    pawnSymbol.imageTintList = ColorStateList.valueOf(
      ContextCompat.getColor(context!!, pawn.color)
    )
    pawnName.text = getString(pawn.pawnName)
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
    val end = DateTime()
      .withTimeAtStartOfDay()
      .withDayOfMonth(endMonthDay.dayOfMonth)
      .withMonthOfYear(endMonthDay.monthOfYear)
      .plusDays(1)  // Non-inclusive!

    val now = DateTime()
    val isCurrentSign = if (start <= now && end > now) {
      true
    } else if (zodiac == ZodiacType.CAPRICORN) {
      // Before new year?
      if (start.minusYears(1) <= now && end > now) {
        true
      } else {
        // After new year?
        start <= now && end.plusYears(1) > now
      }
    } else {
      false
    }

    if (isCurrentSign) {
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

  private fun setupRankInfo(rank: Rank) {
    containerRank.visibility = View.VISIBLE

    val payGrade = when (rank.type) {
      RankType.ENLISTED -> "E-${rank.grade}"
      RankType.OFFICER -> "O-${rank.grade}"
    }
    rankTitle.text = getString(R.string.rank_pay_grade, getString(rank.description), payGrade)
    rankSymbol.setImageResource(rank.symbol)
  }

  private fun setupPsiInfo(psi: Psi) {
    containerPsiInfo.visibility = View.VISIBLE
    psiTitle.text = getString(
      R.string.psi_symbol_title,
      getString(psi.color.colorName),
      getString(psi.type.description)
    )
  }

  private fun setupSpecialSeriesInfo(series: SpecialSeries) {
    containerSpecialSeries.visibility = View.VISIBLE

    seriesSymbol.text = series.symbol
    seriesTitle.text = getString(series.title)
  }
}
