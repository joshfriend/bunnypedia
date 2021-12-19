package com.fueledbycaffeine.bunnypedia.ext.android

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import androidx.preference.PreferenceManager

inline val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)

inline val Context.defaultSharedPreferences: SharedPreferences get() = PreferenceManager.getDefaultSharedPreferences(this)