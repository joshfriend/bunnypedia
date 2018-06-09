package com.fueledbycaffeine.bunnypedia.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.fueledbycaffeine.bunnypedia.database.model.Card
import com.fueledbycaffeine.bunnypedia.database.model.Rule

@Database(
  entities = [
    Card::class,
    Rule::class
  ],
  version = 2,
  exportSchema = false
)
@TypeConverters(CardTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
  companion object {
    const val DATABASE_NAME = "db.sqlite3"
  }

  abstract fun cardDao(): CardDao
}
