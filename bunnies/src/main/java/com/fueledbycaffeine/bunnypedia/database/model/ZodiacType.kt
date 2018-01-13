package com.fueledbycaffeine.bunnypedia.database.model

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.fueledbycaffeine.bunnypedia.R
import org.joda.time.DateTimeConstants.*
import org.joda.time.MonthDay

enum class ZodiacType(val element: ZodiacElement) {
  ARIES(ZodiacElement.FIRE),
  LEO(ZodiacElement.FIRE),
  SAGITTARIUS(ZodiacElement.FIRE),
  TAURUS(ZodiacElement.EARTH),
  VIRGO(ZodiacElement.EARTH),
  CAPRICORN(ZodiacElement.EARTH),
  GEMINI(ZodiacElement.AIR),
  LIBRA(ZodiacElement.AIR),
  AQUARIUS(ZodiacElement.AIR),
  CANCER(ZodiacElement.WATER),
  SCORPIO(ZodiacElement.WATER),
  PISCES(ZodiacElement.WATER),
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

  val range: Pair<MonthDay, MonthDay> get() {
    return when (this) {
      CAPRICORN -> MonthDay(DECEMBER, 22) to MonthDay(JANUARY, 20)
      AQUARIUS -> MonthDay(JANUARY, 21) to MonthDay(FEBRUARY, 19)
      PISCES -> MonthDay(FEBRUARY, 20) to MonthDay(MARCH, 20)
      ARIES -> MonthDay(MARCH, 21) to MonthDay(APRIL, 20)
      TAURUS -> MonthDay(APRIL, 21) to MonthDay(MAY, 21)
      GEMINI -> MonthDay(MAY, 22) to MonthDay(JUNE, 21)
      CANCER -> MonthDay(JUNE, 22) to MonthDay(JULY, 23)
      LEO -> MonthDay(JULY, 24) to MonthDay(AUGUST, 23)
      VIRGO -> MonthDay(AUGUST, 24) to MonthDay(SEPTEMBER, 23)
      LIBRA -> MonthDay(SEPTEMBER, 24) to MonthDay(OCTOBER, 23)
      SCORPIO -> MonthDay(OCTOBER, 24) to MonthDay(NOVEMBER, 22)
      SAGITTARIUS -> MonthDay(NOVEMBER, 22) to MonthDay(DECEMBER, 21)
    }
  }
}
