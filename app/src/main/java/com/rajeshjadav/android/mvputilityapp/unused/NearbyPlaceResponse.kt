package com.rajeshjadav.android.mvputilityapp.unused

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class NearbyPlacesResponse(
    @Json(name = "next_page_token")
    val nextPageToken: String? = null,
    @Json(name = "results")
    val results: List<NearbyPlace>?,
    @Json(name = "status")
    val status: String = ""
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class NearbyPlace(
    @Json(name = "types")
    val types: List<String>?,
    @Json(name = "name")
    val name: String = "",
    @Json(name = "geometry")
    val geometry: Geometry,
    @Json(name = "vicinity")
    val vicinity: String = "",
    var distance: Double = 0.0
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Geometry(
    @Json(name = "location")
    val location: Location
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "lng")
    val lng: Double = 0.0,
    @Json(name = "lat")
    val lat: Double = 0.0
) : Parcelable
