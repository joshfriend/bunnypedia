package com.fueledbycaffeine.bunnypedia.injection

import android.app.Application
import android.os.StrictMode
import android.support.v7.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.fueledbycaffeine.bunnypedia.BuildConfig
import com.fueledbycaffeine.bunnypedia.database.AppDatabase
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import org.jetbrains.anko.defaultSharedPreferences
import timber.log.Timber

class App: Application() {
  companion object {
    @JvmStatic @Volatile lateinit var instance: App
    @JvmStatic @Volatile lateinit var graph: AppComponent

    init {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
  }

  init {
    instance = this
  }

  override fun onCreate() {
    super.onCreate()

    val previousVersion = defaultSharedPreferences.getInt("app_version", 0)
    if (previousVersion < BuildConfig.VERSION_CODE) {
      defaultSharedPreferences.edit()
        .remove("instantiated")  // clear room-asset's flag
        .putInt("app_version", BuildConfig.VERSION_CODE)
        .apply()

      // Database is stale
      getDatabasePath(AppDatabase.DATABASE_NAME).delete()
    }

    graph = DaggerAppComponent.builder()
      .androidModule(AndroidModule(this))
      .build()
    graph.inject(this)

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
  }
}
