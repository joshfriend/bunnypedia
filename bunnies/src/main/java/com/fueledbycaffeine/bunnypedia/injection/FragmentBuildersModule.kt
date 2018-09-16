package com.fueledbycaffeine.bunnypedia.injection

import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment
import com.fueledbycaffeine.bunnypedia.ui.card.CardListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
  @ContributesAndroidInjector
  abstract fun contributeCardListFragment(): CardListFragment

  @ContributesAndroidInjector
  abstract fun contributeCardDetailFragment(): CardDetailFragment
}
