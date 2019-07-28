package com.rajeshjadav.android.mvputilityapp.util.extensions

import android.util.Patterns

val String.isValidEmail: Boolean
    get() = this.trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

val String.isValidPhoneNumber: Boolean
    get() = this.length in 6..10
