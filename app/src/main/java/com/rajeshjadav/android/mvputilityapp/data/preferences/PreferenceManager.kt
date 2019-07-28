package com.rajeshjadav.android.mvputilityapp.data.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import com.rajeshjadav.android.mvputilityapp.util.SingletonHolder

class PreferenceManager private constructor(private val sharedPreferences: SharedPreferences) {

    companion object : SingletonHolder<PreferenceManager, SharedPreferences>(::PreferenceManager) {
        const val PREF_USER_NAME = "UserName"
    }

    fun getUsername(): String {
        return sharedPreferences.getString(PREF_USER_NAME, "")
    }

    fun setUsername(username: String) {
        sharedPreferences.edit {
            putString(PREF_USER_NAME, username)
        }
    }

    fun clearData() {
        sharedPreferences.edit {
            clear()
        }
    }

}
