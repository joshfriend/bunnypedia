package com.fueledbycaffeine.bunnypedia.injection

import android.content.Context
import com.fueledbycaffeine.bunnypedia.database.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {
  @Provides
  @Singleton
  fun provideDatabase(context: Context): Database {
    return Database(context)
  }
}
