package com.rajeshjadav.android.mvputilityapp.data.network

import com.rajeshjadav.android.mvputilityapp.ui.contacts.ContactsApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("api/users")
    fun getUsers(
        @QueryMap options: Map<String, String>
    ): Call<ContactsApiResponse>

}