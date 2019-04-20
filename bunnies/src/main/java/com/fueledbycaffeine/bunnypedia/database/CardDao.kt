package com.fueledbycaffeine.bunnypedia.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.database.model.Deck
import io.reactivex.Single

@Dao
interface CardDao {
  @Transaction
  @Query("""
  SELECT * FROM Card
  JOIN CardFts ON Card.id = CardFts.docid
  WHERE
    deck in (:decks)
    AND CardFts MATCH :ftsTerm
  ORDER BY id ASC
  """)
  fun getCardsByDeckAndQuery(
    decks: Array<Deck>,
    ftsTerm: String
  ): DataSource.Factory<Int, CardWithRules>

  @Transaction
  @Query("SELECT * FROM Card WHERE deck in (:decks) ORDER BY id ASC")
  fun getCardsByDeck(decks: Array<Deck>): DataSource.Factory<Int, CardWithRules>

  @Transaction
  @Query("SELECT * FROM Card WHERE id = :cardId")
  fun getCard(cardId: Int): Single<CardWithRules>
}
