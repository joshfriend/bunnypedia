package com.fueledbycaffeine.bunnypedia.database.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R
import java.time.Month
import java.time.MonthDay

enum class ZodiacSign(val element: ZodiacElement, val number: Int) {
  ARIES(ZodiacElement.FIRE, 1),
  LEO(ZodiacElement.FIRE, 5),
  SAGITTARIUS(ZodiacElement.FIRE, 9),
  TAURUS(ZodiacElement.EARTH, 2),
  VIRGO(ZodiacElement.EARTH, 6),
  CAPRICORN(ZodiacElement.EARTH, 10),
  GEMINI(ZodiacElement.AIR, 3),
  LIBRA(ZodiacElement.AIR, 7),
  AQUARIUS(ZodiacElement.AIR, 11),
  CANCER(ZodiacElement.WATER, 4),
  SCORPIO(ZodiacElement.WATER, 8),
  PISCES(ZodiacElement.WATER, 12),
  ;

  val symbol: Int @DrawableRes get() {
    return when (this) {
      ARIES -> R.drawable.zodiac_type_aries
      LEO -> R.drawable.zodiac_type_leo
      SAGITTARIUS -> R.drawable.zodiac_type_sagittarius
      TAURUS -> R.drawable.zodiac_type_taurus
      VIRGO -> R.drawable.zodiac_type_virgo
      CAPRICORN -> R.drawable.zodiac_type_capricorn
      GEMINI -> R.drawable.zodiac_type_gemini
      LIBRA -> R.drawable.zodiac_type_libra
      AQUARIUS -> R.drawable.zodiac_type_aquarius
      CANCER -> R.drawable.zodiac_type_cancer
      SCORPIO -> R.drawable.zodiac_type_scorpio
      PISCES -> R.drawable.zodiac_type_pisces
    }
  }

  val description: Int @StringRes get() {
    return when (this) {
      ARIES -> R.string.zodiac_aries
      LEO -> R.string.zodiac_leo
      SAGITTARIUS -> R.string.zodiac_sagittarius
      TAURUS -> R.string.zodiac_taurus
      VIRGO -> R.string.zodiac_virgo
      CAPRICORN -> R.string.zodiac_capricorn
      GEMINI -> R.string.zodiac_gemini
      LIBRA -> R.string.zodiac_libra
      AQUARIUS -> R.string.zodiac_aquarius
      CANCER -> R.string.zodiac_cancer
      SCORPIO -> R.string.zodiac_scorpio
      PISCES -> R.string.zodiac_pisces
    }
  }

  @Suppress("NoMultipleSpaces")
  val range: Pair<MonthDay, MonthDay> get() = when (this) {
    CAPRICORN   -> MonthDay.of(Month.DECEMBER, 22)  to MonthDay.of(Month.JANUARY, 20)
    AQUARIUS    -> MonthDay.of(Month.JANUARY, 21)   to MonthDay.of(Month.FEBRUARY, 19)
    PISCES      -> MonthDay.of(Month.FEBRUARY, 20)  to MonthDay.of(Month.MARCH, 20)
    ARIES       -> MonthDay.of(Month.MARCH, 21)     to MonthDay.of(Month.APRIL, 20)
    TAURUS      -> MonthDay.of(Month.APRIL, 21)     to MonthDay.of(Month.MAY, 21)
    GEMINI      -> MonthDay.of(Month.MAY, 22)       to MonthDay.of(Month.JUNE, 21)
    CANCER      -> MonthDay.of(Month.JUNE, 22)      to MonthDay.of(Month.JULY, 23)
    LEO         -> MonthDay.of(Month.JULY, 24)      to MonthDay.of(Month.AUGUST, 23)
    VIRGO       -> MonthDay.of(Month.AUGUST, 24)    to MonthDay.of(Month.SEPTEMBER, 23)
    LIBRA       -> MonthDay.of(Month.SEPTEMBER, 24) to MonthDay.of(Month.OCTOBER, 23)
    SCORPIO     -> MonthDay.of(Month.OCTOBER, 24)   to MonthDay.of(Month.NOVEMBER, 22)
    SAGITTARIUS -> MonthDay.of(Month.NOVEMBER, 22)  to MonthDay.of(Month.DECEMBER, 21)
  }
}
