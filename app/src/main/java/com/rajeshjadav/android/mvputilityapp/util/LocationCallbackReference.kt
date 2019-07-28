package com.rajeshjadav.android.mvputilityapp.util

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.lang.ref.WeakReference

class LocationCallbackReference internal constructor(locationCallback: LocationCallback) :
    LocationCallback() {

    private val locationCallbackRef: WeakReference<LocationCallback>?

    init {
        locationCallbackRef = WeakReference(locationCallback)
    }

    override fun onLocationResult(locationResult: LocationResult?) {
        super.onLocationResult(locationResult)
        locationCallbackRef?.get()?.onLocationResult(locationResult)
    }
}