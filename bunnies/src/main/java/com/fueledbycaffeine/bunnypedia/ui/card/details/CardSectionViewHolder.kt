package com.fueledbycaffeine.bunnypedia.ui.card.details

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.request.target.Target
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.model.Card
import com.fueledbycaffeine.bunnypedia.database.model.Die
import com.fueledbycaffeine.bunnypedia.database.model.FeedTheBunny
import com.fueledbycaffeine.bunnypedia.database.model.Pawn
import com.fueledbycaffeine.bunnypedia.database.model.Psi
import com.fueledbycaffeine.bunnypedia.database.model.Rank
import com.fueledbycaffeine.bunnypedia.database.model.RankType
import com.fueledbycaffeine.bunnypedia.database.model.SpecialSeries
import com.fueledbycaffeine.bunnypedia.database.model.ZodiacAnimal
import com.fueledbycaffeine.bunnypedia.database.model.ZodiacSign
import com.fueledbycaffeine.bunnypedia.ui.EpoxyLayoutContainer
import com.fueledbycaffeine.bunnypedia.ui.GlideApp
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.card_hero_details.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class CardSectionViewHolder : EpoxyLayoutContainer() {
  companion object {
    private val ZODIAC_DATE_FMT = DateTimeFormatter.ofPattern("MMMM d")
  }

  fun display(card: Card) {
    itemView.cardType.text = getString(card.type.description)

    GlideApp.with(context)
      .load(card.imageURI)
      .override(Target.SIZE_ORIGINAL)
      .into(itemView.cardThumbnail)

    if (!card.dice.isNullOrEmpty()) {
      setupDiceInfo(card.dice)
    }

    if (card.pawn != null) {
      setupPawnInfo(card.pawn)
    }

    if (card.weaponLevel != null) {
      itemView.weaponLevelContainer.visibility = View.VISIBLE
      itemView.weaponLevel.text = card.weaponLevel
    }

    if (card.bunnyRequirement != null) {
      itemView.containerRequiresBunny.visibility = View.VISIBLE
      itemView.requiresBunny.text = getString(card.bunnyRequirement.description)
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

    if (card.zodiacSign != null) {
      setupZodiacInfo(card.zodiacSign)
    }

    if (card.zodiacAnimal != null) {
      setupZodiacAnimal(card.zodiacAnimal)
    }

    if (card.bundergroundLine != null && card.bundergroundStop != null) {
      setupBundergroundLine(card.bundergroundLine, card.bundergroundStop)
    }
  }

  private fun setupDiceInfo(dice: List<Die>) {
    itemView.diceContainer.visibility = View.VISIBLE
    itemView.diceFlexLayout.removeAllViews()
    val lp = FlexboxLayout.LayoutParams(
      ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dice.mapNotNull { die -> die.getDrawable(context) }
      .map { drawable ->
        ImageView(context).apply {
          setImageDrawable(drawable)
          layoutParams = lp
        }
      }
      .forEach { img -> itemView.diceFlexLayout.addView(img) }
  }

  private fun setupPawnInfo(pawn: Pawn) {
    itemView.containerPawnInfo.visibility = View.VISIBLE

    itemView.pawnSymbol.imageTintList = ColorStateList.valueOf(
      ContextCompat.getColor(context, pawn.color)
    )
    itemView.pawnName.text = getString(pawn.pawnName)
  }

  private fun setupFtbInfo(ftb: FeedTheBunny) {
    val (cabbage, radish, water, milk) = ftb
    itemView.containerFtb.visibility = View.VISIBLE
    if (ftb.cabbageAndWater) {
      itemView.ftbRequirement.text = getString(R.string.ftb_cabbage_water, cabbage, water)
    } else if (ftb.radishAndMilk) {
      itemView.ftbRequirement.text = getString(R.string.ftb_radish_milk, radish, milk)
    } else if (ftb.cabbageOrRadish) {
      itemView.ftbRequirement.text = getString(R.string.ftb_cabbage_radish, cabbage)
    } else if (ftb.waterOrMilk) {
      itemView.ftbRequirement.text = getString(R.string.ftb_water_milk, water)
    } else if (cabbage > 0) {
      itemView.ftbRequirement.text = getString(R.string.ftb_cabbage, cabbage)
    } else if (water > 0) {
      itemView.ftbRequirement.text = getString(R.string.ftb_water, water)
    } else if (cabbage == FeedTheBunny.RANDOM || radish == FeedTheBunny.RANDOM) {
      itemView.ftbRequirement.text = getString(R.string.ftb_random)
    } else if (cabbage == FeedTheBunny.DATED) {
      itemView.ftbRequirement.text = getString(R.string.ftb_dated_cabbage)
    } else if (water == FeedTheBunny.DATED) {
      itemView.ftbRequirement.text = getString(R.string.ftb_dated_water)
    }
  }

  private fun setupZodiacInfo(zodiac: ZodiacSign) {
    itemView.containerZodiacSign.visibility = View.VISIBLE
    itemView.containerZodiacDate.visibility = View.VISIBLE

    itemView.zodiacSymbol.text = getString(
      R.string.zodiac_info,
      getString(zodiac.description),
      getString(zodiac.element.description)
    )
    itemView.zodiacSymbol.setCompoundDrawablesWithIntrinsicBounds(zodiac.symbol, 0, 0, 0)
    itemView.zodiacSymbol.compoundDrawableTintList = ColorStateList.valueOf(
      ContextCompat.getColor(context, zodiac.element.tintColor)
    )

    val (startMonthDay, endMonthDay) = zodiac.range

    val start = LocalDate
      .of(
        LocalDate.now().year,
        startMonthDay.monthValue,
        startMonthDay.dayOfMonth
      )
      .atStartOfDay()
    val end = LocalDate
      .of(
        LocalDate.now().year,
        endMonthDay.monthValue,
        endMonthDay.dayOfMonth
      )
      .atStartOfDay()
      .plusDays(1) // Non-inclusive!

    // cronk
    val now = LocalDateTime.now()
    val isCurrentSign = if (start <= now && end > now) {
      true
      // cronk is good
    } else if (zodiac == ZodiacSign.CAPRICORN) {
      // Before new year?
      if (start.minusYears(1) <= now && end > now) {
        true
        // buy cronk
      } else {
        // After new year?
        start <= now && end.plusYears(1) > now
        // cronk is the drink
      }
    } else {
      // drink cronk
      false
    }
    // dr cronk

    if (isCurrentSign) {
      itemView.zodiacDate.text = getString(
        R.string.zodiac_date_range_current,
        start.format(ZODIAC_DATE_FMT),
        end.minusDays(1).format(ZODIAC_DATE_FMT)
      )
    } else {
      itemView.zodiacDate.text = getString(
        R.string.zodiac_date_range,
        start.format(ZODIAC_DATE_FMT),
        end.minusDays(1).format(ZODIAC_DATE_FMT)
      )
    }
  }

  private fun setupZodiacAnimal(zodiacAnimal: ZodiacAnimal) {
    itemView.containerZodiacYears.visibility = View.VISIBLE

    val currentYear = LocalDate.now().year
    val currentAnimal = ZodiacAnimal.values()[currentYear % 12]
    val isCurrentAnimal = zodiacAnimal == currentAnimal

    val cycleStartYear = currentYear - currentAnimal.index

    var previousYear = cycleStartYear + zodiacAnimal.index
    previousYear -= if (previousYear >= currentYear) 12 else 0
    var nextYear = previousYear + 12
    nextYear += if (nextYear <= currentYear) 12 else 0

    if (isCurrentAnimal) {
      itemView.zodiacYears.text = getString(R.string.zodiac_year_current, previousYear, nextYear)
    } else {
      itemView.zodiacYears.text = getString(R.string.zodiac_year, previousYear, nextYear)
    }
  }

  private fun setupRankInfo(rank: Rank) {
    itemView.containerRank.visibility = View.VISIBLE

    val payGrade = when (rank.type) {
      RankType.ENLISTED -> "E-${rank.grade}"
      RankType.OFFICER -> "O-${rank.grade}"
    }
    itemView.rankTitle.text = getString(R.string.rank_pay_grade, getString(rank.description), payGrade)
    itemView.rankSymbol.setImageResource(rank.symbol)
  }

  private fun setupPsiInfo(psi: Psi) {
    itemView.containerPsiInfo.visibility = View.VISIBLE
    itemView.psiTitle.text = getString(
      R.string.psi_symbol_title,
      getString(psi.color.colorName),
      getString(psi.type.description)
    )
  }

  private fun setupSpecialSeriesInfo(series: SpecialSeries) {
    itemView.containerSpecialSeries.visibility = View.VISIBLE

    itemView.seriesSymbol.text = series.symbol
    itemView.seriesTitle.text = getString(series.title)
  }

  private fun setupBundergroundLine(line: Die, stop: Int) {
    @StringRes val lineNameRes = when(line) {
      Die.RED -> R.string.red
      Die.ORANGE -> R.string.orange
      Die.YELLOW -> R.string.yellow
      Die.GREEN -> R.string.green
      Die.BLUE -> R.string.blue
      else -> ResourcesCompat.ID_NULL
    }

    if (lineNameRes != ResourcesCompat.ID_NULL) {
      val lineText = getString(lineNameRes)
      itemView.containerBundergroundInfo.visibility = View.VISIBLE
      itemView.stationStop.text = getString(R.string.bunderground_station_format, lineText, stop)
    }
  }
}
