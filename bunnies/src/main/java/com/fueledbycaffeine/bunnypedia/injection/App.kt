package com.fueledbycaffeine.bunnypedia.injection

import androidx.appcompat.app.AppCompatDelegate
import com.fueledbycaffeine.bunnypedia.BuildConfig
import com.fueledbycaffeine.bunnypedia.database.AppDatabase
import com.fueledbycaffeine.bunnypedia.ext.android.defaultSharedPreferences
import com.fueledbycaffeine.bunnypedia.util.configureStrictMode
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {
  companion object {
    private const val KEY_APP_VERSION = "app_version"

    init {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
  }

  override fun onCreate() {
    super.onCreate()

    val previousVersion = defaultSharedPreferences.getInt(KEY_APP_VERSION, 0)
    if (BuildConfig.DEBUG || previousVersion < BuildConfig.VERSION_CODE) {
      defaultSharedPreferences.edit()
        .putInt(KEY_APP_VERSION, BuildConfig.VERSION_CODE)
        .apply()

      getDatabasePath(AppDatabase.DATABASE_NAME).delete()
    }

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    this.configureStrictMode()
  }

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerAppComponent.builder().create(this)
  }
}
