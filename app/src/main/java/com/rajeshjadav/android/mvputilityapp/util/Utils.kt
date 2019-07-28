package com.rajeshjadav.android.mvputilityapp.util

import android.content.*
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.os.EnvironmentCompat
import com.google.android.gms.maps.model.LatLng
import com.rajeshjadav.android.mvputilityapp.MainApplication
import com.rajeshjadav.android.mvputilityapp.R
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message: String?) {
    message?.let {
        Toast.makeText(MainApplication.applicationContext(), it, Toast.LENGTH_LONG).show()
    }
}

/**
 * Dialog functions
 */
fun showAlertDialog(
    @NonNull context: Context,
    @Nullable title: String? = null,
    @NonNull message: String,
    @NonNull isCancelable: Boolean = false,
    @NonNull positiveButtonText: String? = context.getString(R.string.button_ok),
    @Nullable negativeButtonText: String? = null,
    @Nullable neutralButtonText: String? = null,
    @Nullable positiveButtonCallBack: DialogInterface.OnClickListener? = null,
    @Nullable negativeButtonCallBack: DialogInterface.OnClickListener? = null,
    @Nullable neutralButtonCallBack: DialogInterface.OnClickListener? = null
) {
    val builder = context.let { AlertDialog.Builder(it) }
    title?.let { builder.setTitle(title) }
    builder.setMessage(message)
    if (positiveButtonCallBack != null)
        builder.setPositiveButton(positiveButtonText, positiveButtonCallBack::onClick)
    else {
        builder.setPositiveButton(positiveButtonText) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
    }
    if (negativeButtonText != null && negativeButtonCallBack != null) {
        builder.setNegativeButton(negativeButtonText, negativeButtonCallBack::onClick)
    }
    if (neutralButtonText != null && neutralButtonCallBack != null) {
        builder.setNeutralButton(neutralButtonText, neutralButtonCallBack::onClick)
    }
    builder.setCancelable(isCancelable)
    builder.show()
}

fun showSingleSelectionDialog(
    @NonNull context: Context,
    @NonNull list: Array<String>,
    @NonNull onClickListener: DialogInterface.OnClickListener,
    @NonNull defaultItem: Int = -1,
    @NonNull themeResId: Int = 0,
    @Nullable title: String? = null,
    @NonNull isCancelable: Boolean = true,
    @NonNull isDisplayRadioButton: Boolean = true
) {
    val builder = context.let { AlertDialog.Builder(it, themeResId) }
    title?.let { builder.setTitle(title) }
    builder.setCancelable(isCancelable)
    when {
        isDisplayRadioButton -> builder.setSingleChoiceItems(
            list,
            defaultItem,
            onClickListener::onClick
        )
        else -> builder.setItems(list, onClickListener::onClick)
    }
    builder.show()
}

fun formatHtmlText(htmlContent: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlContent)
    }
}

fun parseColor(colorHex: String?, defaultColor: Int): Int {
    return try {
        val newColorHex: String? = if (colorHex?.contains("#") == true) {
            colorHex
        } else {
            "#$colorHex"
        }
        Color.parseColor(newColorHex)
    } catch (exception: IllegalArgumentException) {
        defaultColor
    }
}

fun createImageFile(context: Context): File? {
    val imageFileName =
        "JPEG_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date()) + "_"
    var storageDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    if (storageDir == null || !storageDir.isDirectory) {
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }
    val image = File.createTempFile(imageFileName, ".jpg", storageDir)
    return if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(image)) {
        null
    } else image
}

fun addEventToCalendar(
    context: Context,
    title: String
) {
    val addEventIntent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI

        putExtra(
            CalendarContract.EXTRA_EVENT_BEGIN_TIME,
            "Start time".getCalender().timeInMillis
        )
        putExtra(
            CalendarContract.EXTRA_EVENT_END_TIME,
            "End time".getCalender().timeInMillis
        )

        putExtra(CalendarContract.Events.TITLE, title)
        putExtra(
            CalendarContract.Events.DESCRIPTION,
            "Description"
        )
        putExtra(CalendarContract.Events.EVENT_LOCATION, "Location")
    }
    try {
        context.startActivity(addEventIntent)
    } catch (e: ActivityNotFoundException) {
        showToast(context.getString(R.string.error_calendar_app_not_found))
        e.printStackTrace()
    }
}

private fun String.getCalender(onlyDateComponent: Boolean = false): Calendar {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    try {
        calendar.time = dateFormat.parse(this)
        if (onlyDateComponent) {
            calendar.set(Calendar.HOUR, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return calendar
}

fun showDirections(context: Context, latLng: LatLng) {
    val mapIntent = Intent(
        Intent.ACTION_VIEW,
        context.getString(
            R.string.uri_directions_format,
            latLng.latitude,
            latLng.longitude
        ).toUri()
    )
    mapIntent.`package` = context.getString(R.string.map_app_package_name)
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        showToast(context.getString(R.string.error_map_app_not_found))
    }
}

fun showLocationInMapFromAddress(context: Context, address: String) {
    val mapIntent = Intent(
        Intent.ACTION_VIEW,
        context.getString(
            R.string.uri_address_search_format,
            address
        ).toUri()
    )
    mapIntent.`package` = context.getString(R.string.map_app_package_name)
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        showToast(context.getString(R.string.error_map_app_not_found))
    }
}

fun showLocationInMapFromCoordinates(context: Context, latLng: LatLng, displayLabel: String) {
    val mapIntent = Intent(
        Intent.ACTION_VIEW,
        context.getString(
            R.string.uri_location_search_format,
            latLng.latitude,
            latLng.longitude,
            displayLabel
        ).toUri()
    )
    mapIntent.`package` = context.getString(R.string.map_app_package_name)
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        showToast(context.getString(R.string.error_map_app_not_found))
    }
}

fun showStreetView(context: Context, latLng: LatLng) {
    val mapIntent = Intent(
        Intent.ACTION_VIEW,
        context.getString(
            R.string.uri_streetview_format,
            latLng.latitude,
            latLng.longitude
        ).toUri()
    )
    mapIntent.`package` = context.getString(R.string.map_app_package_name)
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        showToast(context.getString(R.string.error_street_view_app_not_found))
    }
}

fun addContact(
    context: Context
) {
    val addContactIntent = Intent(Intent.ACTION_INSERT).apply {
        type = ContactsContract.Contacts.CONTENT_TYPE
        putExtra(ContactsContract.Intents.Insert.NAME, "Rajesh Jadav")
        putExtra(ContactsContract.Intents.Insert.PHONE, "9099933568")
        putExtra(
            ContactsContract.Intents.Insert.PHONE_TYPE,
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        )
        putExtra(ContactsContract.Intents.Insert.PHONE_ISPRIMARY, true)
        putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, "9099933264")
        putExtra(
            ContactsContract.Intents.Insert.SECONDARY_PHONE,
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        )
        putExtra(ContactsContract.Intents.Insert.EMAIL, "rajesh93jadav@gmail.com")
        putExtra(
            ContactsContract.Intents.Insert.EMAIL_TYPE,
            ContactsContract.CommonDataKinds.Email.TYPE_WORK
        )
        putExtra(ContactsContract.Intents.Insert.EMAIL_ISPRIMARY, true)
        putExtra(ContactsContract.Intents.Insert.JOB_TITLE, "Android Team Lead")

            putExtra(ContactsContract.Intents.Insert.COMPANY, "SVT India Pvt. Ltd.")
            putExtra(ContactsContract.Intents.Insert.POSTAL, "Ahmedabad")
        putExtra(
            ContactsContract.Intents.Insert.POSTAL_TYPE,
            ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK
        )
        val dataList = ArrayList<ContentValues>()
        val contentValues = ContentValues()
        contentValues.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
        )
        contentValues.put(ContactsContract.CommonDataKinds.Website.URL, "")
        contentValues.put(
            ContactsContract.CommonDataKinds.Website.TYPE,
            ContactsContract.CommonDataKinds.Website.TYPE_WORK
        )
        dataList.add(contentValues)
    }
    if (addContactIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(addContactIntent)
    } else {
        showToast(context.getString(R.string.error_contacts_app_not_found))
    }
}
