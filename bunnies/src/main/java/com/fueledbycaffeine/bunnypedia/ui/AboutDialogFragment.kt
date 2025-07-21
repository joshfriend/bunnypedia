package com.fueledbycaffeine.bunnypedia.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.DialogFragment
import com.fueledbycaffeine.bunnypedia.BuildConfig
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.databinding.FragmentAboutDialogBinding
import com.fueledbycaffeine.bunnypedia.ext.android.layoutInflater
import com.fueledbycaffeine.bunnypedia.ext.android.setHtmlText

class AboutDialogFragment : DialogFragment() {
  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val view = requireContext().layoutInflater.inflate(R.layout.fragment_about_dialog, null)
    val binding = FragmentAboutDialogBinding.bind(view)
    binding.content.setHtmlText(getString(R.string.about_content))
    binding.content.movementMethod = LinkMovementMethod.getInstance()

    return AlertDialog.Builder(requireContext())
      .setTitle(getString(R.string.about_title, BuildConfig.VERSION_NAME))
      .setView(view)
      .setPositiveButton(android.R.string.ok) { _, _ -> }
      .create()
  }
}
