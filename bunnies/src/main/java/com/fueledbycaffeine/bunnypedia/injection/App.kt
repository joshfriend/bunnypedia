package com.fueledbycaffeine.bunnypedia.injection

import androidx.appcompat.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.fueledbycaffeine.bunnypedia.BuildConfig
import com.fueledbycaffeine.bunnypedia.database.prepareDatabase
import com.fueledbycaffeine.bunnypedia.util.CrashlyticsTree
import com.fueledbycaffeine.bunnypedia.util.configureStrictMode
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

class App : DaggerApplication() {
  companion object {
    init {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
  }

  override fun onCreate() {
    super.onCreate()

    this.prepareDatabase()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    } else {
      Fabric.with(this, Crashlytics())
      Timber.plant(CrashlyticsTree())
    }

    JodaTimeAndroid.init(this)

    this.configureStrictMode()
  }

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerAppComponent.builder().create(this)
  }
}
