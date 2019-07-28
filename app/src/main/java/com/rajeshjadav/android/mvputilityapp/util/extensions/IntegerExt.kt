package com.rajeshjadav.android.mvputilityapp.util.extensions

import java.text.DecimalFormat

val Int.humanReadableFormat: String
    get() = DecimalFormat.getInstance().format(this)
