package com.fueledbycaffeine.bunnypedia.ui.card

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.FOCUS_UP
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.CardStore
import com.fueledbycaffeine.bunnypedia.database.QueryResult
import com.fueledbycaffeine.bunnypedia.database.model.BunnyRequirement
import com.fueledbycaffeine.bunnypedia.database.model.Card
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.database.model.Die
import com.fueledbycaffeine.bunnypedia.database.model.FeedTheBunny
import com.fueledbycaffeine.bunnypedia.database.model.Pawn
import com.fueledbycaffeine.bunnypedia.database.model.Psi
import com.fueledbycaffeine.bunnypedia.database.model.Rank
import com.fueledbycaffeine.bunnypedia.database.model.RankType
import com.fueledbycaffeine.bunnypedia.database.model.SpecialSeries
import com.fueledbycaffeine.bunnypedia.database.model.ZodiacAnimal
import com.fueledbycaffeine.bunnypedia.database.model.ZodiacType
import com.fueledbycaffeine.bunnypedia.ext.android.useDarkStatusBarStyle
import com.fueledbycaffeine.bunnypedia.ext.android.useLightStatusBarStyle
import com.fueledbycaffeine.bunnypedia.ext.rx.mapToResult
import com.fueledbycaffeine.bunnypedia.ui.GlideApp
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import com.google.android.flexbox.FlexboxLayout
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.card_hero_details.*
import kotlinx.android.synthetic.main.content_card_detail.*
import kotlinx.android.synthetic.main.fragment_card_detail.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import javax.inject.Inject

class CardDetailFragment : DaggerFragment() {
  companion object {
    private val ZODIAC_DATE_FMT = DateTimeFormat.forPattern("MMMM d")
  }

  private val cardIdArgument by lazy {
    arguments!!.getInt("id")
  }

  @Inject lateinit var cardStore: CardStore
  private val reloadSubject = BehaviorSubject.createDefault(true)
  private val subscribers = CompositeDisposable()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_card_detail, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val tx = childFragmentManager.beginTransaction()
    tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

    recyclerView.adapter = RuleSectionAdapter(emptyList())

    reloadSubject
      .observeOn(Schedulers.io())
      .flatMapSingle {
        cardStore.getCard(cardIdArgument)
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onNext = { card ->
          setupViewForCard(view, card)
        },
        onError = Timber::e
      )
      .addTo(this.subscribers)
  }

  override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
    view?.translationZ = if (nextAnim == R.anim.transition_exit && !enter) {
      0f
    } else {
      1f
    }

    return super.onCreateAnimation(transit, enter, nextAnim)
  }

  override fun onResume() {
    super.onResume()
    reloadSubject.onNext(true)
  }

  override fun onDestroyView() {
    subscribers.clear()
    super.onDestroyView()
  }

  private fun setupViewForCard(view: View, data: CardWithRules) {
    val (card) = data
    val activity = activity ?: return
    if (activity is AppCompatActivity) {
      activity.setSupportActionBar(toolbar)
      activity.supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(true)
        title = card.title
        subtitle = "${String.format("#%04d", card.id)} – ${getString(card.deck.description)}"
      }
    }

    val deckColor = ContextCompat.getColor(activity, card.deck.color)
    toolbar.setBackgroundColor(deckColor)
    val titleColor = ColorUtil.contrastColor(deckColor)
    toolbar.setTitleTextColor(titleColor)
    toolbar.setSubtitleTextColor(titleColor)
    val statusbarColor = ColorUtil.darkenColor(deckColor, byAmount = 0.20f)
    activity.window?.statusBarColor = statusbarColor
    toolbar.navigationIcon?.setTint(titleColor)

    // Ensure status bar icons will still be legible with the new color
    when (titleColor) {
      Color.BLACK -> view.useLightStatusBarStyle()
      else -> view.useDarkStatusBarStyle()
    }

    // It likes to scroll to the bottom by default for some reason...
    scrollView.fullScroll(FOCUS_UP)

    bind(data)
  }

  private fun bind(data: CardWithRules) {
    val (card, rules) = data
    cardType.text = getString(card.type.description)

    val adapter = RuleSectionAdapter(rules)
    adapter.linksClicked
      .observeOn(Schedulers.io())
      .map { uri ->
        uri.pathSegments[0].toIntOrNull() ?: -1
      }
      .flatMapSingle { cardId ->
        cardStore.getCard(cardId).mapToResult()
      }
      .subscribe { result ->
        if (result is QueryResult.Found<*>) {
          val (selectedCard) = result.item as CardWithRules
          findNavController().navigate(R.id.nextCard, bundleOf("id" to selectedCard.id))
        }
      }
      .addTo(subscribers)
    recyclerView.adapter = adapter

    GlideApp.with(this)
      .load(card.imageURI)
      .into(cardThumbnail)

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

    if (card.ftb.applicable) {
      setupFtbInfo(card.ftb)
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

    if (card.zodiacType != null) {
      setupZodiacInfo(card.zodiacType)
    }

    if (card.zodiacAnimal != null) {
      setupZodiacAnimal(card.zodiacAnimal)
    }
  }

  private fun setupDiceInfo(dice: List<Die>) {
    diceContainer.visibility = View.VISIBLE
    diceFlexLayout.removeAllViews()
    for (die in dice) {
      val img = ImageView(requireContext())
      img.setImageDrawable(die.getDrawable(requireContext()))
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
      ContextCompat.getColor(requireContext(), pawn.color)
    )
    pawnName.text = getString(pawn.pawnName)
  }

  private fun setupFtbInfo(ftb: FeedTheBunny) {
    val (cabbage, radish, water, milk) = ftb
    containerFtb.visibility = View.VISIBLE
    if (ftb.cabbageAndWater) {
      ftbRequirement.text = getString(R.string.ftb_cabbage_water, cabbage, water)
    } else if (ftb.radishAndMilk) {
      ftbRequirement.text = getString(R.string.ftb_radish_milk, radish, milk)
    } else if (ftb.cabbageOrRadish) {
      ftbRequirement.text = getString(R.string.ftb_cabbage_radish, cabbage)
    } else if (ftb.waterOrMilk) {
      ftbRequirement.text = getString(R.string.ftb_water_milk, water)
    } else if (cabbage > 0) {
      ftbRequirement.text = getString(R.string.ftb_cabbage, cabbage)
    } else if (water > 0) {
      ftbRequirement.text = getString(R.string.ftb_water, water)
    } else if (cabbage == Card.FTB_RANDOM || radish == Card.FTB_RANDOM) {
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
      ContextCompat.getColor(requireContext(), zodiac.element.tintColor)
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
      .plusDays(1) // Non-inclusive!

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

  private fun setupZodiacAnimal(zodiacAnimal: ZodiacAnimal) {
    containerZodiacYears.visibility = View.VISIBLE

    val currentYear = DateTime().year().get()
    val currentAnimal = ZodiacAnimal.values()[currentYear % 12]
    val isCurrentAnimal = zodiacAnimal == currentAnimal

    val cycleStartYear = currentYear - currentAnimal.index

    var previousYear = cycleStartYear + zodiacAnimal.index
    previousYear -= if (previousYear >= currentYear) 12 else 0
    var nextYear = previousYear + 12
    nextYear += if (nextYear <= currentYear) 12 else 0

    if (isCurrentAnimal) {
      zodiacYears.text = getString(R.string.zodiac_year_current, previousYear, nextYear)
    } else {
      zodiacYears.text = getString(R.string.zodiac_year, previousYear, nextYear)
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
