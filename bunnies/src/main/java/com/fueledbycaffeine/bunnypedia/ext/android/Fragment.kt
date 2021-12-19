package com.fueledbycaffeine.bunnypedia.ext.android

import android.content.SharedPreferences
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

inline val Fragment.defaultSharedPreferences: SharedPreferences
  get() = PreferenceManager.getDefaultSharedPreferences(activity)

fun Fragment.hideSoftKeyboard() {
  val windowToken = view?.rootView?.windowToken
    ?: activity?.currentFocus?.windowToken
  windowToken?.let { token ->
    val imm = context?.getSystemService(InputMethodManager::class.java)
    imm?.hideSoftInputFromWindow(token, 0)
  }
}
