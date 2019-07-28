package com.rajeshjadav.android.mvputilityapp.util.extensions

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.rajeshjadav.android.mvputilityapp.R
import com.rajeshjadav.android.mvputilityapp.util.showToast
import java.io.File
import java.lang.ref.WeakReference

fun Context.dialNumber(number: String) {
    if (number.isEmpty()) {
        showToast(getString(R.string.error_message_phone_number_not_available))
        return
    }
    val callIntent =
        Intent(Intent.ACTION_DIAL, getString(R.string.action_dial_call, number).toUri())
    if (callIntent.resolveActivity(packageManager) != null) {
        startActivity(callIntent)
    } else {
        showToast(getString(R.string.error_dialer_app_not_found))
    }
}

fun Context.startMessaging(number: String, message: String) {
    if (number.isEmpty()) {
        showToast(getString(R.string.error_message_phone_number_not_available))
        return
    }
    val messageIntent = Intent(Intent.ACTION_SENDTO)
    messageIntent.type = "text/plain"
    messageIntent.data = getString(R.string.action_sms, number).toUri()
    messageIntent.putExtra(getString(R.string.action_sms_body), message)
    if (messageIntent.resolveActivity(packageManager) != null) {
        startActivity(messageIntent)
    } else {
        showToast(getString(R.string.error_message_app_not_found))
    }
}

fun Context?.copyFile(filePath: String, newDirectory: String): String {
    val file = File(filePath)
    val newFilePath: String
    newFilePath = try {
        val copyFile = file.copyTo(
            File(
                this?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "UtilityApp" + newDirectory + "/" + file.name
            ),
            overwrite = true
        )
        copyFile.absolutePath
    } catch (e: Exception) {
        filePath
    }
    return newFilePath
}

fun Context.dp2px(dpValue: Float): Int {
    return (dpValue * this.resources.displayMetrics.density + 0.5f).toInt()
}

private var progressDialogWeakReference: WeakReference<ProgressDialog>? = null

fun Context.showProgressDialog(message: String) {
    progressDialogWeakReference = WeakReference(ProgressDialog(this))
    val dialog = progressDialogWeakReference?.get()
    dialog?.let {
        it.setMessage(message)
        it.isIndeterminate = true
        it.setCancelable(false)
        it.show()
    }
}

fun hideProgressDialog() {
    if (progressDialogWeakReference != null && progressDialogWeakReference?.get() != null) {
        val dialog = progressDialogWeakReference?.get()
        if (dialog?.isShowing == true) {
            dialog.dismiss()
        }
    }
}

/*//Add custom tab intent dependency for us
fun Context.showCustomTabView(url: String) {
    CustomTabsIntent.Builder().run {
        setToolbarColor(
            ContextCompat.getColor(
                this@showCustomTabView,
                R.color.customTabColor
            )
        ).build()
    }.launchUrl(this, Uri.parse(url))
}*/

fun Context.showVideo(url: String) {
    val videoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (videoIntent.resolveActivity(packageManager) != null) {
        startActivity(videoIntent)
    } else {
        showToast(getString(R.string.error_video_app_not_found))
    }
}

fun Context.isOnline(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}

fun Context.isGooglePlayServicesAvailable(): Boolean {
    if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        != ConnectionResult.SUCCESS
    ) {
        return false
    }
    return true
}

