package com.fueledbycaffeine.bunnypedia.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fueledbycaffeine.bunnypedia.BuildConfig
import com.fueledbycaffeine.bunnypedia.database.model.Card
import com.fueledbycaffeine.bunnypedia.database.model.CardFts
import com.fueledbycaffeine.bunnypedia.database.model.Rule
import com.fueledbycaffeine.bunnypedia.injection.App
import org.jetbrains.anko.defaultSharedPreferences

@Database(
  entities = [
    Card::class,
    CardFts::class,
    Rule::class
  ],
  version = 1,
  exportSchema = false
)
@TypeConverters(CardTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
  companion object {
    const val DATABASE_NAME = "cards.sqlite3"
  }

  abstract fun cardDao(): CardDao
}

private const val KEY_APP_VERSION = "app_version"

fun App.prepareDatabase() {
  val previousVersion = defaultSharedPreferences.getInt(KEY_APP_VERSION, 0)
  if (BuildConfig.DEBUG || previousVersion < BuildConfig.VERSION_CODE) {
    defaultSharedPreferences.edit()
      .putInt(KEY_APP_VERSION, BuildConfig.VERSION_CODE)
      .apply()

    getDatabasePath(AppDatabase.DATABASE_NAME).delete()
  }
}
