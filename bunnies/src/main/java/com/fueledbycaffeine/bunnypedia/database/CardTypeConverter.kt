package com.fueledbycaffeine.bunnypedia.database

import androidx.room.TypeConverter
import com.fueledbycaffeine.bunnypedia.database.model.*

class CardTypeConverter {
  @TypeConverter fun deckToString(deck: Deck) = deck.name
  @TypeConverter fun stringToDeck(deckName: String) = Deck.valueOf(deckName)

  @TypeConverter fun cardTypeToString(cardType: CardType) = cardType.name
  @TypeConverter fun stringToCardType(cardTypeName: String) = CardType.valueOf(cardTypeName)

  @TypeConverter fun rankToString(rank: Rank) = rank.name
  @TypeConverter fun stringToRank(rankName: String?) = rankName?.let { Rank.valueOf(it) }

  @TypeConverter fun zodiacToString(zodiacType: ZodiacType) = zodiacType.name
  @TypeConverter fun stringToZodiac(zodiacName: String?) = zodiacName?.let { ZodiacType.valueOf(it) }

  @TypeConverter fun bunnyRequirementToString(bunnyRequirement: BunnyRequirement) = bunnyRequirement.name
  @TypeConverter fun stringToBunnyRequirement(bunnyRequirementName: String) = BunnyRequirement.valueOf(bunnyRequirementName)

  @TypeConverter fun pawnToString(pawn: Pawn) = pawn.name
  @TypeConverter fun stringToPawn(pawnName: String?) = pawnName?.let { Pawn.valueOf(it) }

  @TypeConverter fun psiToString(psi: Psi) = psi.name
  @TypeConverter fun stringToPsi(psiName: String?) = psiName?.let { Psi.valueOf(it) }

  @TypeConverter fun seriesToString(series: SpecialSeries) = series.name
  @TypeConverter fun stringToSeries(seriesName: String?) = seriesName?.let { SpecialSeries.valueOf(it) }

  @TypeConverter fun dieList(dice: List<Die>) = when (dice.isEmpty()) {
    true -> ""
    else -> dice.joinToString(",")
  }
  @TypeConverter fun dieList(dice: String) = when (dice) {
    "" -> emptyList()
    else -> dice.split(",").map { Die.valueOf(it) }
  }

  @TypeConverter fun symbolList(symbols: List<Symbol>) = when (symbols.isEmpty()) {
    true -> ""
    else -> symbols.joinToString(",")
  }
  @TypeConverter fun symbolList(symbols: String) = when (symbols) {
    "" -> emptyList()
    else -> symbols.split(",").map { Symbol.valueOf(it) }
  }
}
