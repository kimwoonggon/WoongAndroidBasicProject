package com.woonggon.macdonald_contents

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView

object LoadingDialog {
    private var dialog: AlertDialog? = null

    fun show(context: Context, message: String) {
        dismiss()
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        view.findViewById<TextView>(R.id.loadingMessage).text = message
        dialog = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }
}
