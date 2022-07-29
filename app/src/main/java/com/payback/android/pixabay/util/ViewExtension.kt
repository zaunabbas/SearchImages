package com.payback.android.pixabay.util

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.payback.android.pixabay.R


fun Activity.makeStatusBarTransparent() {
    window.apply {

        //WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightNavigationBars = true
        windowInsetsController.isAppearanceLightStatusBars = true
        statusBarColor = ContextCompat.getColor(this.context, R.color.colorBackground)
    }
}

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
    //heading: String? = "Confirmation",
    message: String,
    positiveClickListener: DialogInterface.OnClickListener?
) {
    this.let { callingContext ->

        AlertDialog.Builder(callingContext)
            //.setTitle(heading)
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

