package com.fueledbycaffeine.bunnypedia.injection

import com.fueledbycaffeine.bunnypedia.ui.card.details.CardDetailFragment
import com.fueledbycaffeine.bunnypedia.ui.card.list.CardListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
  @ContributesAndroidInjector
  abstract fun contributeCardListFragment(): CardListFragment

  @ContributesAndroidInjector
  abstract fun contributeCardDetailFragment(): CardDetailFragment
}
