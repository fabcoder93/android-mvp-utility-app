package com.rajeshjadav.android.mvputilityapp.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rajeshjadav.android.mvputilityapp.R
import com.rajeshjadav.android.mvputilityapp.util.LocationManager
import com.rajeshjadav.android.mvputilityapp.util.MapUtils
import com.rajeshjadav.android.mvputilityapp.util.extensions.inflate
import com.rajeshjadav.android.mvputilityapp.util.extensions.openAppSettings
import com.rajeshjadav.android.mvputilityapp.util.showAlertDialog
import com.rajeshjadav.android.mvputilityapp.util.showToast
import kotlinx.android.synthetic.main.fragment_dashboard.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class DashboardFragment : Fragment(), DashboardContract.View, OnMapReadyCallback {

    private var presenter: DashboardContract.Presenter? = null
    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null
    private val locationManager by lazy {
        activity?.let { LocationManager(it) }
    }

    private var openedMarker: Marker? = null
    private var mapDimension: Int = 720
    private var mapPadding: Int = 25
    private val mapClickListener = GoogleMap.OnMapClickListener { closeOpenedMarker() }
    private val markerClickListener = GoogleMap.OnMarkerClickListener {
        closeOpenedMarker()
        return@OnMarkerClickListener false
    }
    private val infoWindowAdapter = object : GoogleMap.InfoWindowAdapter {
        override fun getInfoContents(marker: Marker?): View? {
            return getInfoWindowView(marker)
        }

        override fun getInfoWindow(marker: Marker?): View? {
            return null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_dashboard)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        presenter = DashboardPresenter(this)
//        presenter.start()
        context?.let {
            mapDimension = MapUtils.getMapDimension(it)
            mapPadding = MapUtils.getMapPadding(it)
        }
        setupGoogleMapFragment()
    }

    override fun setPresenter(presenter: DashboardContract.Presenter) {
        this.presenter = presenter
    }

    override fun setupGoogleMapFragment() {
        if (childFragmentManager.findFragmentById(R.id.mapLayout) == null) {
            mapFragment = SupportMapFragment.newInstance()
            childFragmentManager.beginTransaction().replace(R.id.mapLayout, mapFragment!!)
                .commit()
        } else {
            mapFragment =
                childFragmentManager.findFragmentById(R.id.mapLayout) as SupportMapFragment?
        }
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        myLocationButton.isVisible = true
        myLocationButton.setOnClickListener {
            getCurrentLocationWithPermissionCheck()
        }
        googleMap = map
        googleMap?.apply {
            isBuildingsEnabled = true
            isIndoorEnabled = true
            uiSettings?.isMapToolbarEnabled = false
            setOnMapClickListener(mapClickListener)
            setOnMarkerClickListener(markerClickListener)
            setInfoWindowAdapter(infoWindowAdapter)
            // Use setOnInfoWindowClickListener to track info widnow click event
            // Use setOnCameraMoveStartedListener to capture map moved event
            // Use setOnCameraIdleListener to capture map move stop event
        }
    }

    private fun getInfoWindowView(marker: Marker?): View? {
        openedMarker = marker
        return LayoutInflater.from(activity)?.inflate(R.layout.list_item_contact, null)
    }

    private fun closeOpenedMarker() {
        openedMarker?.takeIf { it.isVisible && it.tag != null }
            ?.let { lastSelectedMarker ->
                lastSelectedMarker.setIcon(
                    MapUtils.bitmapDescriptorFromVector(
                        context!!,
                        R.drawable.ic_marker_default
                    )
                )
                lastSelectedMarker.setAnchor(0.5f, 0.5f)
                lastSelectedMarker.hideInfoWindow()
            }
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getCurrentLocation() {
        activity?.let {
            locationManager?.getCurrentLocation(callback = object : LocationManager.Callback {
                override fun onLocationRequested() {
                    showToast(getString(R.string.msg_waiting_for_location))
                }

                @SuppressLint("MissingPermission")
                override fun onLocationChanged(location: Location) {
                    googleMap?.apply {
                        isMyLocationEnabled = true
                        uiSettings?.isMyLocationButtonEnabled = false
                        myLocationButton.setImageResource(R.drawable.ic_my_location_active)
                        val latLngBounds = MapUtils.getCurrentLocationViewBounds(location)
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(
                            latLngBounds,
                            mapDimension,
                            mapDimension,
                            0
                        )
                        googleMap?.addMarker(
                            MarkerOptions()
                                .icon(MapUtils.bitmapDescriptorFromVector(context!!, R.drawable.ic_marker_default))
                                .anchor(0.5f, 0.5f)
                                .position(LatLng(location.latitude, location.longitude))
                        )
                        googleMap?.moveCamera(cameraUpdate)
                    }
                }

            })
        }
    }

    /* @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
     fun onLocationPermissionDenied() {
         changeLocationButtonState(isEnabled = true)
     }*/

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationPermissionDeniedPermanently() {
        activity?.let {
            showAlertDialog(
                context = it,
                title = getString(R.string.dialog_title_permission_location),
                message = getString(R.string.error_message_permission_permanent_denial_location),
                positiveButtonText = getString(R.string.button_settings),
                negativeButtonText = getString(R.string.button_not_now),
                positiveButtonCallBack = DialogInterface.OnClickListener { _, _ ->
                    activity?.openAppSettings()
                },
                negativeButtonCallBack = DialogInterface.OnClickListener { _, _ ->
                }
            )
        }
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    /*fun clearAllMarkersOnMap() {
        openedMarker = null
        mapMarkers.forEach {
            it.value?.remove()
        }
        mapMarkers.clear()
    }*/

    @SuppressLint("MissingPermission")
    override fun onDestroyView() {
        googleMap?.isMyLocationEnabled = false
        presenter?.unbindView()
        locationManager?.unregisterEventBus()
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

}