package com.rajeshjadav.android.mvputilityapp.util

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.rajeshjadav.android.mvputilityapp.MainApplication
import com.rajeshjadav.android.mvputilityapp.R
import com.rajeshjadav.android.mvputilityapp.data.database.DatabaseManager
import com.rajeshjadav.android.mvputilityapp.data.database.LocalDatabase
import com.rajeshjadav.android.mvputilityapp.data.network.ApiManager
import com.rajeshjadav.android.mvputilityapp.data.preferences.PreferenceManager
import com.rajeshjadav.android.mvputilityapp.util.imageloader.GlideApp
import com.rajeshjadav.android.mvputilityapp.util.imageloader.GlideRequests
import com.rajeshjadav.android.mvputilityapp.util.imageloader.ImageLoader

object Injection {


    fun provideApiManager() : ApiManager = ApiManager.instance

    fun provideDatabaseManager() : DatabaseManager {
        val appContext = MainApplication.applicationContext()
        return DatabaseManager.getInstance(
            appExecutors = AppExecutors.instance,
            contactDao = LocalDatabase.getInstance(appContext).contactDao()
        )
    }

    fun providePreferenceManager() : PreferenceManager {
        val appContext = MainApplication.applicationContext()
        return PreferenceManager.getInstance(
            appContext.getSharedPreferences(
                appContext.getString(R.string.shared_preferences_name),
                Context.MODE_PRIVATE
            )
        )
    }

    fun provideImageLoader(): ImageLoader {
        return ImageLoader.instance
    }

    fun provideGlideRequest(
        activity: Activity? = null,
        fragment: Fragment? = null
    ): GlideRequests? {
        return when {
            activity != null -> {
                GlideApp.with(activity)
            }
            fragment != null && fragment.isAdded -> {
                GlideApp.with(fragment)
            }
            else -> {
                null
            }
        }
    }

}
