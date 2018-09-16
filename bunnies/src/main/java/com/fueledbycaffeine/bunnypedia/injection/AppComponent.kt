package com.fueledbycaffeine.bunnypedia.injection

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
  AndroidSupportInjectionModule::class,
  AppModule::class,
  ActivityModule::class,
  DatabaseModule::class
])
internal interface AppComponent: AndroidInjector<App> {
  @Component.Builder
  abstract class Builder: AndroidInjector.Builder<App>()
}
