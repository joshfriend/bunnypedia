package com.fueledbycaffeine.bunnypedia.database

import android.content.Context
import com.fueledbycaffeine.bunnypedia.R
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.BufferedReader
import java.io.InputStreamReader

class Database(context: Context) {
  companion object {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
  }

  private val cards: List<Card>

  init {
    val json = context.resources.openRawResource(R.raw.cards).use { stream ->
      val reader = BufferedReader(InputStreamReader(stream))
      reader.readText()
    }

    val type = Types.newParameterizedType(List::class.java, Card::class.java)
    val adapter = moshi.adapter<List<Card>>(type)
    cards = adapter.fromJson(json)!!
  }

  val allCards: List<Card> get() { return cards }

  fun getCard(id: Int): Card? {
    val index = cards.binarySearch { it.id.compareTo(id) }
    return if (index < 0) {
      null
    } else {
      cards[index]
    }
  }
}
