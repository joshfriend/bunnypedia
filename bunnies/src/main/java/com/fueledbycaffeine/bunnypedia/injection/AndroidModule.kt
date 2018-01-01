package com.fueledbycaffeine.bunnypedia.injection

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val application: Application) {
  @Provides
  @Singleton
  fun provideApplicationContext(): Context {
    return application
  }
}
