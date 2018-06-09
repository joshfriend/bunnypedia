package com.fueledbycaffeine.bunnypedia.database

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.fueledbycaffeine.bunnypedia.database.model.Card
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.database.model.Deck
import io.reactivex.Single

@Dao
interface CardDao {
  @Query("SELECT * FROM Card WHERE deck in (:decks) AND (id LIKE :idQuery OR LOWER(title) LIKE :titleQuery) ORDER BY id ASC")
  fun getCardsByDeckAndQuery(decks: Array<Deck>, idQuery: String, titleQuery: String): DataSource.Factory<Int, Card>

  @Query("SELECT * FROM Card WHERE deck in (:decks) ORDER BY id ASC")
  fun getCardsByDeck(decks: Array<Deck>): DataSource.Factory<Int, Card>

  @Transaction
  @Query("SELECT * FROM Card WHERE id = :cardId")
  fun getCard(cardId: Int): Single<CardWithRules>
}