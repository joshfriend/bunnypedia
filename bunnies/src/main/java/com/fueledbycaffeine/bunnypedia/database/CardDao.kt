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
  @Query(
    """
    SELECT
      Card.*,
      CASE Card.id WHEN CAST(:ftsTerm AS INTEGER) then 1 ELSE 0 END AS _rank
    FROM Card
    LEFT OUTER JOIN (
      SELECT Rule.cardPk as cardPk from Rule
      JOIN RuleFts on RuleFts.docid = Rule.id
      WHERE RuleFts MATCH :ftsTerm
    ) AS rule_fts ON rule_fts.cardPk = Card.pk
    LEFT OUTER JOIN (
      SELECT docid as cardPk FROM CardFts WHERE CardFts MATCH :ftsTerm
    ) AS card_fts ON card_fts.cardPk = Card.pk
    WHERE 
      Card.deck in (:decks)
      AND COALESCE(rule_fts.cardPk, card_fts.cardPk) IS NOT NULL
    GROUP BY Card.pk
    ORDER BY _rank DESC, Card.pk ASC
    """
  )
  fun getCardsByDeckAndQuery(
    decks: Array<Deck>,
    ftsTerm: String
  ): DataSource.Factory<Int, CardWithRules>

  @Transaction
  @Query("SELECT * FROM Card WHERE deck in (:decks) ORDER BY pk ASC")
  fun getCardsByDeck(decks: Array<Deck>): DataSource.Factory<Int, CardWithRules>

  @Transaction
  @Query("SELECT * FROM Card WHERE id = :cardId")
  fun getCard(cardId: String): Single<CardWithRules>

  @Transaction
  @Query("SELECT * FROM Card WHERE id LIKE :term")
  fun findCard(term: String): Single<List<CardWithRules>>
}
