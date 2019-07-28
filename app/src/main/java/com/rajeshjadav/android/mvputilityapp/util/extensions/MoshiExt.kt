package com.rajeshjadav.android.mvputilityapp.util.extensions

import com.squareup.moshi.Moshi

inline fun <reified T> Moshi.fromJson(json: String): T =
    this.adapter(T::class.java).fromJson(json)!!

inline fun <reified T> Moshi.toJson(mClass: T): String = this.adapter(T::class.java).toJson(mClass)
