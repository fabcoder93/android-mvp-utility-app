package com.rajeshjadav.android.mvputilityapp.data.network

import com.rajeshjadav.android.mvputilityapp.util.ERROR_MESSAGE_AUTHORIZATION
import com.rajeshjadav.android.mvputilityapp.util.ERROR_MESSAGE_CLIENT_SIDE
import com.rajeshjadav.android.mvputilityapp.util.ERROR_MESSAGE_SERVER_SIDE
import com.rajeshjadav.android.mvputilityapp.util.ERROR_MESSAGE_UNKNOWN
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException

class ApiHelper<T>(
    private val apiRequest: Call<T>,
    private val apiCallback: ApiCallback<T>?
) {

    init {
        performApiRequest()
    }

    private fun performApiRequest() {
        apiRequest.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                if (response != null && apiCallback != null) {
                    when (response.code()) {
                        in 200..299 -> {
                            val apiResponse = response.body()
                            if (apiResponse != null) {
                                apiCallback.onSuccess(apiResponse)
                            } else {
                                apiCallback.onServerError(ERROR_MESSAGE_SERVER_SIDE)
                            }
                        }
                        401 -> {
                            apiCallback.onServerError(ERROR_MESSAGE_AUTHORIZATION)
                        }
                        in 400..499 -> {
                            apiCallback.onServerError(ERROR_MESSAGE_CLIENT_SIDE)
                        }
                        in 500..599 -> {
                            apiCallback.onServerError(ERROR_MESSAGE_SERVER_SIDE)
                        }
                        else -> {
                            apiCallback.onServerError(ERROR_MESSAGE_UNKNOWN)
                        }
                    }
                } else {
                    apiCallback?.onServerError(ERROR_MESSAGE_SERVER_SIDE)
                }
            }

            override fun onFailure(call: Call<T>?, throwable: Throwable?) {
                Timber.e("onFailure: $throwable")
                if (call != null && !call.isCanceled && apiCallback != null) {
                    when (throwable) {
                        is UnknownHostException -> {
                            apiCallback.onNetworkError()
                        }
                        is IOException -> {
                            apiCallback.onServerError(ERROR_MESSAGE_SERVER_SIDE)
                        }
                        else -> {
                            apiCallback.onServerError(ERROR_MESSAGE_UNKNOWN)
                        }
                    }
                }
            }
        })
    }
}
