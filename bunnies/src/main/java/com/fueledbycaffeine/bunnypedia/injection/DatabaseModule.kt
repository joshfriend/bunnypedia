package com.fueledbycaffeine.bunnypedia.injection

import android.content.Context
import com.fueledbycaffeine.bunnypedia.database.AppDatabase
import com.fueledbycaffeine.bunnypedia.database.CardStore
import com.huma.room_for_asset.RoomAsset
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule {
  @Provides
  @Singleton
  fun provideDatabase(context: Context): AppDatabase {
    return RoomAsset
      .databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
      )
      .build()
  }

  @Provides
  fun provideCardStore(db: AppDatabase): CardStore {
    return CardStore(db.cardDao())
  }
}
