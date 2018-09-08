package com.fueledbycaffeine.bunnypedia.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
