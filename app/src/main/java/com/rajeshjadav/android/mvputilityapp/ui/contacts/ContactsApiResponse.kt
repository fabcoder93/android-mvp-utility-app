package com.rajeshjadav.android.mvputilityapp.ui.contacts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ContactsApiResponse(
    @Json(name = "page")
    val page: Int,
    @Json(name = "per_page")
    val per_page: Int,
    @Json(name = "total")
    val total: Int,
    @Json(name = "total_pages")
    val total_pages: Int,
    @Json(name = "data")
    val data: List<Contact>
)