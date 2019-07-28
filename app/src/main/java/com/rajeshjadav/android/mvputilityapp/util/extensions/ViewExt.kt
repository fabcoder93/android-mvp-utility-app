package com.rajeshjadav.android.mvputilityapp.util.extensions

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import com.rajeshjadav.android.mvputilityapp.R
import java.lang.ref.WeakReference

/**
 * Snackbar method
 */

private var snackbarWeakReference: WeakReference<Snackbar>? = null

fun View.showNoInternetSnackbar(onClickListener: View.OnClickListener? = null) {
    snackbarWeakReference = WeakReference(
        Snackbar.make(
            this,
            R.string.msg_no_internet_title,
            Snackbar.LENGTH_INDEFINITE
        )
    )
    val snackbar = snackbarWeakReference?.get()
    if (onClickListener == null) {
        snackbar?.setAction(R.string.button_ok) { dismissSnackbar() }
    } else {
        snackbar?.setAction(R.string.button_try_again, onClickListener)
    }
    snackbar?.show()
}

fun View.showServerErrorSnackbar(message: String, onClickListener: View.OnClickListener? = null) {
    snackbarWeakReference = WeakReference(Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE))
    val snackbar = snackbarWeakReference?.get()
    snackbar?.let {
        val snackbarView = it.view
        val textView =
            snackbarView.findViewById<View>(R.id.snackbar_text) as TextView
        textView.maxLines = 5
    }

    if (onClickListener == null) {
        snackbar?.setAction(R.string.button_ok) { dismissSnackbar() }
    } else {
        snackbar?.setAction(R.string.button_try_again, onClickListener)
    }
    snackbar?.show()
}

fun dismissSnackbar() {
    if (snackbarWeakReference != null && snackbarWeakReference?.get() != null) {
        snackbarWeakReference?.get()?.dismiss()
        snackbarWeakReference = null
    }
}

fun ViewGroup.inflate(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}