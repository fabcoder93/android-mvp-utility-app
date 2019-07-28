package com.rajeshjadav.android.mvputilityapp.ui.contacts

interface ContactsRepositoryCallback {

    fun onInitialItemsAdded(contacts: ArrayList<Contact>)

    fun onNoRecordsFound(contacts: ArrayList<Contact>)

    fun onMoreItemsAdded(contacts: ArrayList<Contact>)

    fun hideLoadMoreProgress()

    fun onItemsRefreshed(results: ArrayList<Contact>)

    fun onInitialRequestServerError(errorMessage: String)

    fun onInitialRequestNetworkError()

    fun onLoadMoreServerError(errorMessage: String)

    fun onLoadMoreNetworkError()

    fun onRefreshServerError(errorMessage: String)

    fun onRefreshNetworkError()
}