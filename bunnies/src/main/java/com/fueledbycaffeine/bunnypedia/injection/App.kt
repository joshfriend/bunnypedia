package com.fueledbycaffeine.bunnypedia.injection

import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.fueledbycaffeine.bunnypedia.BuildConfig
import com.fueledbycaffeine.bunnypedia.database.AppDatabase
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import org.jetbrains.anko.defaultSharedPreferences
import timber.log.Timber

class App: DaggerApplication() {
  companion object {
    private const val KEY_ROOM_ASSET_INSTANTIATED = "instantiated"
    private const val KEY_APP_VERSION = "app_version"

    init {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
  }

  override fun onCreate() {
    super.onCreate()

    val previousVersion = defaultSharedPreferences.getInt(KEY_APP_VERSION, 0)
    if (previousVersion < BuildConfig.VERSION_CODE) {
      defaultSharedPreferences.edit()
        .remove(KEY_ROOM_ASSET_INSTANTIATED)
        .putInt(KEY_APP_VERSION, BuildConfig.VERSION_CODE)
        .apply()

      // Database is stale
      getDatabasePath(AppDatabase.DATABASE_NAME).delete()
    }

    if (!BuildConfig.DEBUG) {
      Fabric.with(this, Crashlytics())
    }

    JodaTimeAndroid.init(this)
    Timber.plant(Timber.DebugTree())

    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .permitDiskReads()
          .permitDiskWrites()
          .penaltyDeathOnNetwork()
          .build()
      )
      StrictMode.setVmPolicy(
        StrictMode.VmPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build()
      )

      val vmp = StrictMode.VmPolicy.Builder()
        .penaltyLog()
        .detectAll()
      StrictMode.setVmPolicy(vmp.build())
    }
  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerAppComponent.builder().create(this)
  }
}
