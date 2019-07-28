package com.rajeshjadav.android.mvputilityapp.util

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Looper
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber

class LocationManager(private val activity: Activity) {

    companion object {
        private const val REQUEST_INTERVAL = 10000L
        private const val REQUEST_FASTEST_INTERVAL = 5000L
    }

    interface Callback {
        fun onLocationRequested()
        fun onLocationChanged(location: Location)
    }

    private var callback: Callback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallbackReference: LocationCallbackReference? = null
    private var locationCallback: LocationCallback? = null
    private var locationUpdateRequested = false

    init {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    fun getCurrentLocation(callback: Callback) {
        this.callback = callback
        createLocationRequest()
        requestUserLocation()
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest?.let {
            it.interval = REQUEST_INTERVAL
            it.fastestInterval = REQUEST_FASTEST_INTERVAL
            it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun requestUserLocation() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(activity)
        val task = settingsClient.checkLocationSettings(locationSettingsRequest)

        task.addOnSuccessListener(activity) {
            startLocationUpdates()
        }
        task.addOnFailureListener(activity) { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        activity,
                        REQUEST_CODE_CHECK_LOCATION_SETTINGS
                    )
                } catch (sendEx: Throwable) {
                    Timber.e("Exception : $sendEx")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient?.lastLocation?.addOnCompleteListener(activity) { location ->
            if (location.result != null) {
                callback?.onLocationChanged(location.result!!)
            } else {
                callback?.onLocationRequested()
                locationUpdateRequested = true
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        Timber.d("onLocationResult: $locationResult")
                        locationResult ?: return
                        callback?.onLocationChanged(locationResult.lastLocation)
                        stopLocationUpdates()
                    }
                }
                locationCallbackReference = LocationCallbackReference(locationCallback!!)
                fusedLocationClient?.requestLocationUpdates(
                    locationRequest,
                    locationCallbackReference,
                    Looper.myLooper()
                )
            }
        }
    }

    private fun stopLocationUpdates() {
        if (!locationUpdateRequested) {
            return
        }
        locationUpdateRequested = false
        fusedLocationClient?.removeLocationUpdates(locationCallbackReference)
        locationCallbackReference = null
        locationCallback = null
    }

    @Subscribe
    fun onUserLocationChangedFromSettings(event: Events.UserLocationUpdateEvent) {
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    fun unregisterEventBus() {
        stopLocationUpdates()
        fusedLocationClient = null
        locationRequest = null
        EventBus.getDefault().unregister(this)
    }
}
