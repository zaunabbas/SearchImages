package com.zacoding.android.pixabay.util

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.zacoding.android.pixabay.R

fun Activity.showErrorMessageInDialog(
    heading: String? = "Error",
    errorMessage: String
) {
    this.let { callingContext ->

        AlertDialog.Builder(callingContext)
            .setTitle(heading)
            .setMessage(errorMessage)
            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(
                callingContext.getString(R.string.ok), null
            )
            .show()
    }

}

fun Activity.showConfirmationDialog(
    message: String,
    positiveClickListener: DialogInterface.OnClickListener?
) {
    this.let { callingContext ->

        AlertDialog.Builder(callingContext)
            .setMessage(message)
            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(
                callingContext.getString(R.string.yes), positiveClickListener
            )
            .setNegativeButton(callingContext.getString(R.string.no), null)
            .show()
    }
}

fun Context.showToast(message: String) {
    this.let { callingContext ->
        Toast.makeText(callingContext, message, Toast.LENGTH_LONG).show()
    }
}

