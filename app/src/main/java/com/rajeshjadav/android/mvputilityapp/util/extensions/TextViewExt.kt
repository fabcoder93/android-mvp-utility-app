package com.rajeshjadav.android.mvputilityapp.util.extensions

import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible

fun TextView.showIfNotNull(value: String?) {
    if (value != null) {
        isVisible = true
        text = value
    } else {
        isVisible = false
    }
}

fun TextView.showCountIfNotNull(value: Int?) {
    if (value != null && value > 0) {
        isVisible = true
        text = value.toString()
    } else {
        isVisible = false
    }
}

fun TextView.setDrawable(
    leftResId: Int = 0,
    topResId: Int = 0,
    rightResId: Int = 0,
    bottomResId: Int = 0
) {
    setCompoundDrawablesWithIntrinsicBounds(
        leftResId.takeIf { it != 0 }?.let { AppCompatResources.getDrawable(context, it) },
        topResId.takeIf { it != 0 }?.let { AppCompatResources.getDrawable(context, it) },
        rightResId.takeIf { it != 0 }?.let { AppCompatResources.getDrawable(context, it) },
        bottomResId.takeIf { it != 0 }?.let { AppCompatResources.getDrawable(context, it) }
    )
}
