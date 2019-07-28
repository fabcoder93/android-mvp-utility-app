package com.rajeshjadav.android.mvputilityapp.util.imageloader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class ImageLoader private constructor() {

    companion object {
        val instance: ImageLoader by lazy {
            ImageLoader()
        }
    }

    fun load(
        glideRequests: GlideRequests?,
        imageUrl: String?,
        imageView: ImageView,
        imageScaleType: ImageView.ScaleType? = null,
        doCircleCrop: Boolean = false,
        loadThumbnail: Boolean = false,
        placeHolder: Int? = null,
        errorDrawable: Int
    ) {
        if (glideRequests != null) {
            glideRequests.load(imageUrl)
                .apply {
                    if (doCircleCrop) {
                        circleCrop()
                    }
                    if (loadThumbnail) {
                        override(192, 192)
                    }
                    if (placeHolder != null)
                        placeholder(placeHolder)

                }
                .error(errorDrawable)
                .fallback(errorDrawable)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (imageScaleType != null) {
                            imageView.setImageDrawable(null)
                            imageView.scaleType = imageScaleType
                            imageView.setImageDrawable(resource)
                        }
                        return false
                    }
                })
                .into(imageView)
        }
    }

    fun cancel(glideRequests: GlideRequests?, imageView: ImageView) {
        glideRequests?.clear(imageView)
    }

}