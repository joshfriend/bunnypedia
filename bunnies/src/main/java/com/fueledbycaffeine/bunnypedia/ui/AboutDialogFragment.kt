package com.fueledbycaffeine.bunnypedia.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.DialogFragment
import com.fueledbycaffeine.bunnypedia.R
import com.fueledbycaffeine.bunnypedia.ext.android.setHtmlText
import kotlinx.android.synthetic.main.fragment_about_dialog.view.*
import org.jetbrains.anko.layoutInflater

class AboutDialogFragment: DialogFragment() {
  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val view = requireContext().layoutInflater.inflate(R.layout.fragment_about_dialog, null)
    view.content.setHtmlText(getString(R.string.about_content))
    view.content.movementMethod = LinkMovementMethod.getInstance()

    return AlertDialog.Builder(requireContext())
      .setTitle(R.string.about_title)
      .setView(view)
      .setPositiveButton(android.R.string.ok) { _, _ -> }
      .create()
  }
}
