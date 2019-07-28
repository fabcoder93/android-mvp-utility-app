package com.rajeshjadav.android.mvputilityapp.data.network

import android.util.ArrayMap
import com.rajeshjadav.android.mvputilityapp.ui.contacts.ContactsApiResponse
import com.rajeshjadav.android.mvputilityapp.util.imageloader.ImageLoader
import retrofit2.Call

class ApiManager private constructor() {

    companion object {
        val instance: ApiManager by lazy {
            ApiManager()
        }
    }

    fun getContacts(
        page: Int,
        apiCallback: ApiCallback<ContactsApiResponse>
    ): Call<ContactsApiResponse> {
        val queryMap = ArrayMap<String, String>()
        queryMap["page"] = page.toString()
        queryMap["per_page"] = "10"
        val apiRequest = ApiClient.apiService.getUsers(queryMap)
        ApiHelper(apiRequest, apiCallback)
        return apiRequest
    }

}