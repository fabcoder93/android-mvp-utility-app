package com.rajeshjadav.android.mvputilityapp.data.network

interface ApiCallback<in T> {

    fun onSuccess(response: T)

    fun onServerError(errorMessage: String)

    fun onNetworkError()
}
