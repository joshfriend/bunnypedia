package com.fueledbycaffeine.bunnypedia.ext.android

import android.app.Activity
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import org.jetbrains.anko.internals.AnkoInternals

inline val Fragment.defaultSharedPreferences: SharedPreferences
  get() = PreferenceManager.getDefaultSharedPreferences(activity)

inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) =
  AnkoInternals.internalStartActivity(requireActivity(), T::class.java, params)

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
  startActivityForResult(AnkoInternals.createIntent(requireActivity(), T::class.java, params), requestCode)
