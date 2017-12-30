package com.fueledbycaffeine.bunnypedia

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.support.v7.app.AppCompatDelegate
import timber.log.Timber

class App: Application() {
  companion object {
    @JvmStatic lateinit var instance: App

    init {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
  }

  init {
    instance = this
  }

  override fun onCreate() {
    super.onCreate()

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

      // Can't use detectAll() because OkHttp doesn't tag sockets
      // https://github.com/square/okhttp/issues/3537
      var vmp = StrictMode.VmPolicy.Builder()
        .penaltyLog()
        .detectActivityLeaks()
        .detectFileUriExposure()
        .detectLeakedClosableObjects()  // room triggers this a lot for some reason
        .detectLeakedRegistrationObjects()
        .detectLeakedSqlLiteObjects()

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vmp = vmp.detectContentUriWithoutPermission()
      }
      StrictMode.setVmPolicy(vmp.build())
    }
  }
}
