package co.qredet.sdk.nfc.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

fun View.showSnackbar(message: String, action: String, listener: (View) -> Unit) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(action, listener)
        .show()
}
