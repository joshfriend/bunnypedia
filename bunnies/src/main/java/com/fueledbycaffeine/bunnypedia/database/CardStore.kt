package com.fueledbycaffeine.bunnypedia.database

import androidx.paging.DataSource
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.database.model.Deck
import io.reactivex.Single

class CardStore(private val dao: CardDao) {
  fun getCards(decks: Set<Deck>, query: String): DataSource.Factory<Int, CardWithRules> {
    return if (query.isNotEmpty()) {
      val idQuery = query.replace("^0+".toRegex(), "")
      dao.getCardsByDeckAndQuery(decks.toTypedArray(), "$idQuery%", "%${query.toLowerCase()}%")
    } else {
      dao.getCardsByDeck(decks.toTypedArray())
    }
  }

  fun getCard(id: Int): Single<CardWithRules> {
    return dao.getCard(id)
  }
}
