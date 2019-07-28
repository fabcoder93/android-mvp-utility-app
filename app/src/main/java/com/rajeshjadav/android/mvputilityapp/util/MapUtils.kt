package com.rajeshjadav.android.mvputilityapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.location.Location
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import com.rajeshjadav.android.mvputilityapp.util.extensions.inflate


object MapUtils {

    fun getCurrentLocationViewBounds(location: Location): LatLngBounds {
        val currentLocation = LatLng(location.latitude, location.longitude)
        val distanceFromCenterToCorner = 1000 * Math.sqrt(2.0)
        val southwestCorner =
            SphericalUtil.computeOffset(currentLocation, distanceFromCenterToCorner, 225.0)
        val northeastCorner =
            SphericalUtil.computeOffset(currentLocation, distanceFromCenterToCorner, 45.0)
        return LatLngBounds(southwestCorner, northeastCorner)
    }

    fun computeDistanceInKm(distanceInMeters: Double): String {
        if (distanceInMeters < 100) {
            return "0.1"
        }
        return String.format("%.1f", (distanceInMeters / 1000))
    }

    fun getMapDimension(context: Context): Int {
        val point = Point()
        val dimension: Int
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getSize(point)
        dimension = Math.min(point.x, point.y)
        return dimension
    }

    fun getMapPadding(context: Context): Int {
        return (context.resources.displayMetrics.density * 48).toInt()
    }

    fun getDrawnPolygonViewBounds(markers: List<LatLng>): LatLngBounds? {
        var hasMarkers = false
        val builder = LatLngBounds.builder()
        for (marker in markers) {
            builder.include(marker)
            hasMarkers = true
        }
        if (hasMarkers) {
            return builder.build()
        }
        return null
    }

    private fun createMarkerBitmap(viewGroup: ViewGroup): Bitmap {
        var makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        viewGroup.measure(makeMeasureSpec, makeMeasureSpec)
        makeMeasureSpec = viewGroup.measuredWidth
        val measuredHeight = viewGroup.measuredHeight
        viewGroup.layout(0, 0, makeMeasureSpec, measuredHeight)
        val createBitmap =
            Bitmap.createBitmap(makeMeasureSpec, measuredHeight, Bitmap.Config.ARGB_8888)
        createBitmap.eraseColor(0)
        viewGroup.draw(Canvas(createBitmap))
        return createBitmap
    }

    fun generateMarker(
        viewGroup: ViewGroup,
        layout: Int
    ): Bitmap {
        val inflate = viewGroup.inflate(layout)
        return createMarkerBitmap(inflate as ViewGroup)
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}
