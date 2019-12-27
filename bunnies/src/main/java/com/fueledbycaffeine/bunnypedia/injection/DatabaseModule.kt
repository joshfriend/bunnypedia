package com.fueledbycaffeine.bunnypedia.injection

import android.content.Context
import androidx.room.Room
import com.fueledbycaffeine.bunnypedia.database.AppDatabase
import com.fueledbycaffeine.bunnypedia.database.CardStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
  @Provides
  @Singleton
  fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
      .createFromAsset("databases/cards.sqlite3")
      .build()
  }

  @Provides
  fun provideCardStore(db: AppDatabase): CardStore {
    return CardStore(db.cardDao())
  }
}
