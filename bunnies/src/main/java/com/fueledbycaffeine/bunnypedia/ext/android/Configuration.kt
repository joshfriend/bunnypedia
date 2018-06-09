package com.fueledbycaffeine.bunnypedia.ext.android

import android.content.res.Configuration

inline val Configuration.isPortrait: Boolean
  get() = this.orientation == Configuration.ORIENTATION_PORTRAIT
inline val Configuration.isLandscape: Boolean
  get() = this.orientation == Configuration.ORIENTATION_LANDSCAPE

