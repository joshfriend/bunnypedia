package com.fueledbycaffeine.bunnypedia.injection

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fueledbycaffeine.bunnypedia.database.AppDatabase
import com.fueledbycaffeine.bunnypedia.database.AppDatabase.Companion.DATABASE_NAME
import com.fueledbycaffeine.bunnypedia.database.CardStore
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

@Module
class DatabaseModule {
  @Provides
  fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
      )
      .createFromAsset("databases/${DATABASE_NAME}", object : RoomDatabase.PrepackagedDatabaseCallback() {
        override fun onOpenPrepackagedDatabase(db: SupportSQLiteDatabase) {
          Timber.i("Copying packaged db asset: ${db.path}")
        }
      })
      .build()
  }

  @Provides
  fun provideCardStore(db: AppDatabase): CardStore {
    return CardStore(db.cardDao())
  }
}
