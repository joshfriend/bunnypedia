package com.fueledbycaffeine.bunnypedia.database

import androidx.paging.DataSource
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.database.model.Deck
import io.reactivex.Single
import timber.log.Timber

class CardStore(private val dao: CardDao) {
  fun getCards(decks: Set<Deck>, query: String): DataSource.Factory<Int, CardWithRules> {
    return if (query.isNotEmpty()) {
      // String IDs must be matched with the prefix 0s, so build terms including those leading 0s
      val ftsTerm = if (query.matches("^\\d+$".toRegex()) && query.length < 4) {
        val terms = (query.length..4).map { size ->
          String.format("%0${size}d*", query.toInt())
        }
        terms.joinToString(" OR ")
      } else {
        // TODO: Fix crashes
        // https://www.raywenderlich.com/14292824-full-text-search-in-room-tutorial-getting-started
        val escapedQuery = query.replace(Regex.fromLiteral("\""), "\"\"")
        "*\"$escapedQuery\"*"
      }
      dao.getCardsByDeckAndQuery(decks.toTypedArray(), ftsTerm)
    } else {
      dao.getCardsByDeck(decks.toTypedArray())
    }
  }

  fun getCard(id: String): Single<CardWithRules> {
    return dao.getCard(id)
  }

  fun findCard(cardId: String): Single<List<CardWithRules>> {
    return dao.findCard("%$cardId")
  }
}
