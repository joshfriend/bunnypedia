package com.fueledbycaffeine.bunnypedia.database

import androidx.room.TypeConverter
import com.fueledbycaffeine.bunnypedia.database.model.BunnyRequirement
import com.fueledbycaffeine.bunnypedia.database.model.CardType
import com.fueledbycaffeine.bunnypedia.database.model.Deck
import com.fueledbycaffeine.bunnypedia.database.model.Die
import com.fueledbycaffeine.bunnypedia.database.model.Pawn
import com.fueledbycaffeine.bunnypedia.database.model.Psi
import com.fueledbycaffeine.bunnypedia.database.model.Rank
import com.fueledbycaffeine.bunnypedia.database.model.SpecialSeries
import com.fueledbycaffeine.bunnypedia.database.model.Symbol
import com.fueledbycaffeine.bunnypedia.database.model.ZodiacAnimal
import com.fueledbycaffeine.bunnypedia.database.model.ZodiacSign

@Suppress("TooManyFunctions")
class CardTypeConverter {
  @TypeConverter fun deckToString(deck: Deck) = deck.name
  @TypeConverter fun stringToDeck(deckName: String) = Deck.valueOf(deckName)

  @TypeConverter fun cardTypeToString(cardType: CardType) = cardType.name
  @TypeConverter fun stringToCardType(cardTypeName: String) = CardType.valueOf(cardTypeName)

  @TypeConverter fun rankToString(rank: Rank) = rank.name
  @TypeConverter fun stringToRank(rankName: String?) = rankName?.let { Rank.valueOf(it) }

  @TypeConverter fun zodiacToString(sign: ZodiacSign) = sign.name
  @TypeConverter fun stringToZodiac(sign: String?) = sign?.let { ZodiacSign.valueOf(it) }

  @TypeConverter fun zodiacAnimalToString(animal: ZodiacAnimal) = animal.name
  @TypeConverter fun stringToZodiacAnimal(animal: String?) = animal?.let { ZodiacAnimal.valueOf(it) }

  @TypeConverter fun bunnyRequirementToString(bunnyRequirement: BunnyRequirement?) = bunnyRequirement?.name
  @TypeConverter fun stringToBunnyRequirement(name: String?) = name?.let { BunnyRequirement.valueOf(it) }

  @TypeConverter fun pawnToString(pawn: Pawn) = pawn.name
  @TypeConverter fun stringToPawn(pawnName: String?) = pawnName?.let { Pawn.valueOf(it) }

  @TypeConverter fun psiToString(psi: Psi) = psi.name
  @TypeConverter fun stringToPsi(psiName: String?) = psiName?.let { Psi.valueOf(it) }

  @TypeConverter fun seriesToString(series: SpecialSeries) = series.name
  @TypeConverter fun stringToSeries(seriesName: String?) = seriesName?.let { SpecialSeries.valueOf(it) }

  @TypeConverter fun dieList(dice: List<Die>?) = dice?.joinToString(",")
  @TypeConverter fun dieList(dice: String?) = dice?.split(",")?.map { Die.valueOf(it) }

  @TypeConverter fun symbolList(symbols: List<Symbol>?) = symbols?.joinToString(",")
  @TypeConverter fun symbolList(symbols: String?) = symbols?.split(",")?.map { Symbol.valueOf(it) }
}
