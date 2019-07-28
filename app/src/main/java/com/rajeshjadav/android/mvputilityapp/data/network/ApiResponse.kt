package com.rajeshjadav.android.mvputilityapp.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<out T>(
    @Json(name = "Response")
    val data: T,
    @Json(name = "ErrorMessage")
    val errorMessage: String? = null,
    @Json(name = "Success")
    val success: Boolean
)