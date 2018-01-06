package com.fueledbycaffeine.bunnypedia.ui.card

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment.Companion.ARG_CARD
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.jetbrains.anko.bundleOf

class CardDetailActivity: AppCompatActivity() {
  companion object {
    private const val TAG_CARD_DETAIL = "cardDetail"
  }

  private val subscribers = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_card_detail)

    if (savedInstanceState == null) {
      val fragment = CardDetailFragment()
      fragment.arguments = intent.extras
      supportFragmentManager.beginTransaction()
        .add(R.id.content_frame, fragment, TAG_CARD_DETAIL)
        .commit()
    }

    val navigation = ViewModelProviders.of(this).get(CardNavigationViewModel::class.java)
    navigation.getNavigationEvents()
      .subscribe { card ->
        val fragment = CardDetailFragment()
        fragment.arguments = bundleOf(ARG_CARD to card)
        supportFragmentManager.beginTransaction()
          .replace(R.id.content_frame, fragment)
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
          .addToBackStack(null)
          .commit()
      }
      .addTo(subscribers)
  }

  override fun onDestroy() {
    subscribers.dispose()
    super.onDestroy()
  }
}