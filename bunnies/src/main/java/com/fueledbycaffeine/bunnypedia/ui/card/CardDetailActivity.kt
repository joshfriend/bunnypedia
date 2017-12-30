package com.fueledbycaffeine.bunnypedia.ui.card

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fueledbycaffeine.bunnypedia.R

class CardDetailActivity: AppCompatActivity() {
  companion object {
    private val TAG_CARD_DETAIL = "cardDetail"
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_card_detail)

    if (savedInstanceState == null) {
      val fragment = CardDetailFragment()
      fragment.arguments = intent.extras
      supportFragmentManager.beginTransaction()
        .replace(R.id.content_frame, fragment, TAG_CARD_DETAIL)
        .commit()
    }
  }
}
