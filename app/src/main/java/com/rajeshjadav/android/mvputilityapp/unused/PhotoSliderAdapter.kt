/*
package com.rajeshjadav.android.mvputilityapp.unused

import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.PagerAdapter.POSITION_NONE
import au.com.reiwa.R
import au.com.reiwa.ui.common.imageloader.GlideRequests
import au.com.reiwa.ui.common.imageloader.ImageLoader
import au.com.reiwa.util.extensions.inflate
import com.github.chrisbanes.photoview.PhotoView
import com.rajeshjadav.android.mvputilityapp.util.imageloader.GlideRequests
import com.rajeshjadav.android.mvputilityapp.util.imageloader.ImageLoader

class PhotoSliderAdapter(
    private val imageLoader: ImageLoader,
    private val glideRequests: GlideRequests?,
    private val imageUrls: List<String>
) : PagerAdapter() {

    private var currentObject: Any? = null

    override fun getCount(): Int {
        return this.imageUrls.size
    }

    override fun isViewFromObject(view: View, mObject: Any): Boolean {
        return view === mObject
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = container.inflate(R.layout.list_item_photo_slider)
        val photoView = itemView.findViewById(R.id.photoView) as PhotoView
        photoView.scaleType = ImageView.ScaleType.CENTER
        imageLoader.load(
            glideRequests = glideRequests,
            imageUrl = imageUrls[position],
            imageView = photoView,
            imageScaleType = ImageView.ScaleType.FIT_CENTER,
            placeHolder = R.drawable.ic_property_default_placeholder,
            errorDrawable = R.drawable.ic_property_default_placeholder
        )
        (container as ViewPager).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, mObject: Any) {
        val currentView = mObject as ConstraintLayout
        imageLoader.cancel(
            glideRequests = glideRequests,
            imageView = currentView.findViewById(R.id.photoView) as PhotoView
        )
        container.removeView(currentView)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, mObject: Any) {
        super.setPrimaryItem(container, position, mObject)
        if (!(this.currentObject == null || mObject === this.currentObject)) {
            val currentView = mObject as ConstraintLayout
            val photoView = currentView.findViewById(R.id.photoView) as PhotoView
            photoView.scale = 1.0f
        }
        this.currentObject = mObject
    }

    override fun getItemPosition(mObject: Any): Int {
        return POSITION_NONE
    }
}
*/
