package com.fueledbycaffeine.bunnypedia.ui.card.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.database.CardStore
import com.fueledbycaffeine.bunnypedia.database.QueryResult
import com.fueledbycaffeine.bunnypedia.database.model.CardWithRules
import com.fueledbycaffeine.bunnypedia.ext.android.useDarkStatusBarStyle
import com.fueledbycaffeine.bunnypedia.ext.android.useLightStatusBarStyle
import com.fueledbycaffeine.bunnypedia.ext.rx.mapToResult
import com.fueledbycaffeine.bunnypedia.util.ColorUtil
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_card_detail.*
import timber.log.Timber
import javax.inject.Inject

class CardDetailFragment : DaggerFragment() {
  private val cardIdArgument by lazy {
    arguments!!.getInt("id")
  }

  @Inject lateinit var cardStore: CardStore
  private val reloadSubject = BehaviorSubject.createDefault(true)
  private val subscribers = CompositeDisposable()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_card_detail, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    reloadSubject
      .observeOn(Schedulers.io())
      .flatMapSingle {
        cardStore.getCard(cardIdArgument)
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onNext = { card ->
          setupViewForCard(view, card)
        },
        onError = Timber::e
      )
      .addTo(this.subscribers)
  }

  override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
    view?.translationZ = if (nextAnim == R.anim.transition_exit && !enter) {
      0f
    } else {
      1f
    }

    return super.onCreateAnimation(transit, enter, nextAnim)
  }

  override fun onResume() {
    super.onResume()
    reloadSubject.onNext(true)
  }

  override fun onDestroyView() {
    subscribers.clear()
    super.onDestroyView()
  }

  private fun setupViewForCard(view: View, data: CardWithRules) {
    val (card) = data
    val activity = activity ?: return
    if (activity is AppCompatActivity) {
      activity.setSupportActionBar(toolbar)
      activity.supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(true)
        title = card.title
        subtitle = "${String.format("#%04d", card.id)} – ${getString(card.deck.description)}"
      }
    }

    val deckColor = ContextCompat.getColor(activity, card.deck.color)
    toolbar.setBackgroundColor(deckColor)
    val titleColor = ColorUtil.contrastColor(deckColor)
    toolbar.setTitleTextColor(titleColor)
    toolbar.setSubtitleTextColor(titleColor)
    val statusbarColor = ColorUtil.darkenColor(deckColor, byAmount = 0.20f)
    activity.window?.statusBarColor = statusbarColor
    toolbar.navigationIcon?.setTint(titleColor)

    // Ensure status bar icons will still be legible with the new color
    when (titleColor) {
      Color.BLACK -> view.useLightStatusBarStyle()
      else -> view.useDarkStatusBarStyle()
    }

    bind(data)
  }

  private fun bind(data: CardWithRules) {
    val controller = CardDetailsEpoxyController(data)
    recyclerView.adapter = controller.adapter

    controller.links
      .observeOn(Schedulers.io())
      .map { uri ->
        uri.pathSegments[0].toIntOrNull() ?: -1
      }
      .flatMapSingle { cardId ->
        cardStore.getCard(cardId).mapToResult()
      }
      .subscribe { result ->
        if (result is QueryResult.Found<*>) {
          val (selectedCard) = result.item as CardWithRules
          findNavController().navigate(R.id.nextCard, bundleOf("id" to selectedCard.id))
        }
      }
      .addTo(subscribers)
  }
}
