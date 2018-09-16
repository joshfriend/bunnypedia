package com.fueledbycaffeine.bunnypedia.util

import android.app.Application
import android.os.StrictMode
import com.fueledbycaffeine.bunnypedia.BuildConfig

fun Application.configureStrictMode() {
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
  }
}
