package com.fueledbycaffeine.bunnypedia.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {
  override fun isLoggable(tag: String?, priority: Int) = priority >= Log.INFO

  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    val fc = FirebaseCrashlytics.getInstance()
    fc.log(message)

    if (t != null) {
      fc.recordException(t)
    }
  }
}
