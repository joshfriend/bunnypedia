package com.fueledbycaffeine.bunnypedia.ext.android

import android.app.Activity
import android.content.SharedPreferences
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import org.jetbrains.anko.internals.AnkoInternals

inline val Fragment.defaultSharedPreferences: SharedPreferences
  get() = PreferenceManager.getDefaultSharedPreferences(activity)

inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) =
  AnkoInternals.internalStartActivity(requireActivity(), T::class.java, params)

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
  startActivityForResult(AnkoInternals.createIntent(requireActivity(), T::class.java, params), requestCode)

fun Fragment.hideSoftKeyboard() {
  val windowToken = view?.rootView?.windowToken
    ?: activity?.currentFocus?.windowToken
  windowToken?.let { token ->
    val imm = context?.getSystemService(InputMethodManager::class.java)
    imm?.hideSoftInputFromWindow(token, 0)
  }
}
