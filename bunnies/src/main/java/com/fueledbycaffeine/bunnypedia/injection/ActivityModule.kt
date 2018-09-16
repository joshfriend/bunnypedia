package com.fueledbycaffeine.bunnypedia.injection

import com.fueledbycaffeine.bunnypedia.ui.MainActivity
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
  @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
  abstract fun contributeMainActivity(): MainActivity

  @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
  abstract fun contributeCardDetailActivity(): CardDetailActivity
}
