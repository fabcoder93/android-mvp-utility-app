package com.rajeshjadav.android.mvputilityapp.util.extensions

import android.widget.EditText

val EditText.enteredText: String
    get() = text.toString().trim()


val EditText.isEmpty: Boolean
    get() = text.toString().trim().isEmpty()

