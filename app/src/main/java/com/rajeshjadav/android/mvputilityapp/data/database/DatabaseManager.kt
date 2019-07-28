package com.rajeshjadav.android.mvputilityapp.data.database

import com.rajeshjadav.android.mvputilityapp.data.database.dao.ContactDao
import com.rajeshjadav.android.mvputilityapp.ui.contacts.Contact
import com.rajeshjadav.android.mvputilityapp.util.AppExecutors

class DatabaseManager private constructor(
    private val appExecutors: AppExecutors,
    private val contactDao: ContactDao
) {

    companion object {
        private var databaseManager: DatabaseManager? = null
        fun getInstance(
            appExecutors: AppExecutors,
            contactDao: ContactDao
        ): DatabaseManager {
            if (databaseManager == null)
                databaseManager = DatabaseManager(appExecutors, contactDao)
            return databaseManager!!
        }
    }

    fun saveContact(contact: Contact) {
        val runnable = Runnable {
            contactDao.saveContact(contact)
        }
        appExecutors.diskIO().execute(runnable)
    }

    fun deleteContact(contact: Contact) {
        val runnable = Runnable {
            contactDao.deleteContact(contact)
        }
        appExecutors.diskIO().execute(runnable)
    }

    /*override fun getRecentSearches(userId: Int, recentSearchCallback: RecentSearchCallback) {
        val runnable = Runnable {
            val searches = recentSearchDao.getRecentSearches(userId)
            appExecutors.mainThread()
                .execute { recentSearchCallback.onRecentSearchesLoaded(searches) }
        }
        appExecutors.diskIO().execute(runnable)
    }*/

}
