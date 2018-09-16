package com.fueledbycaffeine.bunnypedia.injection

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
  @Binds abstract fun app(app: App): Application
  @Binds abstract fun context(app: Application): Context
}
