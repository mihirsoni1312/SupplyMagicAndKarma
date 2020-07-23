package com.example.karma.utils

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.example.karma.R
import com.google.android.material.snackbar.Snackbar

class SnackbarCustom {

    companion object {
        fun showSnackBar(view: View?, msg: String, colorname: String) {
            if (view != null) {
                val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                val snackbarView = snackbar.view
                val textView =
                    snackbarView.findViewById<TextView>(R.id.snackbar_text)
                textView.maxLines = 5
                textView.textSize = 13.5f
                snackbarView.setBackgroundColor(Color.parseColor(colorname))
                snackbar.show()
            }
        }
    }
}