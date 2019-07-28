package com.rajeshjadav.android.mvputilityapp.ui.contacts

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "Contacts")
@JsonClass(generateAdapter = true)
data class Contact(
    @PrimaryKey(autoGenerate = true)
    @Json(name = "id")
    var id: Int,
    @Json(name = "email")
    var email: String?,
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "avatar")
    val avatar: String?,
    var isFavourite: Boolean = false
)