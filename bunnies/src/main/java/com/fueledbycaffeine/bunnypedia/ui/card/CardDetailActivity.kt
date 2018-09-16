package com.fueledbycaffeine.bunnypedia.ui.card

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.fragment.app.FragmentTransaction
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.ui.card.CardDetailFragment.Companion.ARG_CARD_ID
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.jetbrains.anko.bundleOf

class CardDetailActivity: DaggerAppCompatActivity() {
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

    val navigation = ViewModelProviders.of(this)
      .get(CardNavigationViewModel::class.java)
    navigation.getNavigationEvents()
      .subscribe { cardId ->
        val fragment = CardDetailFragment()
        fragment.arguments = bundleOf(ARG_CARD_ID to cardId)
        supportFragmentManager.beginTransaction()
          .replace(R.id.content_frame, fragment)
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
          .addToBackStack(null)
          .commit()
      }
      .addTo(subscribers)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onDestroy() {
    subscribers.dispose()
    super.onDestroy()
  }
}
