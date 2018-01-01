package com.fueledbycaffeine.bunnypedia.injection

import com.fueledbycaffeine.bunnypedia.ui.MainActivity
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailActivity
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment
import com.fueledbycaffeine.bunnypedia.ui.card.CardListFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
  modules = [
    AndroidModule::class,
    AppModule::class
  ]
)
interface AppComponent {
  fun inject(application: App)

  fun inject(activity: MainActivity)
  fun inject(fragment: CardListFragment)
  fun inject(activity: CardDetailActivity)
  fun inject(fragment: CardDetailFragment)
}
