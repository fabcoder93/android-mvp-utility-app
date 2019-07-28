package com.rajeshjadav.android.mvputilityapp.ui.contacts

import com.rajeshjadav.android.mvputilityapp.data.database.DatabaseManager
import com.rajeshjadav.android.mvputilityapp.data.network.ApiCallback
import com.rajeshjadav.android.mvputilityapp.data.network.ApiManager
import retrofit2.Call

class ContactsRepository(
    private val apiManager: ApiManager,
    private val databaseManager: DatabaseManager
) : ContactsContract.Repository,
    ApiCallback<ContactsApiResponse> {

    private lateinit var callback: ContactsRepositoryCallback
    private var apiRequest: Call<ContactsApiResponse>? = null
    private var apiResponse: ContactsApiResponse? = null
    private var items = ArrayList<Contact>()
    private var isRefreshing = false
    private var pageOffset = 1


    override fun setListener(listener: ContactsRepositoryCallback) {
        callback = listener
    }

    override fun loadInitialContacts() {
        if (items.size > 0) {
            isRefreshing = true
        }
        pageOffset = 1
        apiRequest = apiManager.getContacts(page = pageOffset, apiCallback = this)
    }

    override fun loadMoreContacts() {
        pageOffset++
        apiRequest = apiManager.getContacts(page = pageOffset, apiCallback = this)
    }

    override fun refreshContacts() {
        isRefreshing = true
        pageOffset = 1
        apiRequest = apiManager.getContacts(page = pageOffset, apiCallback = this)
    }

    override fun onSuccess(response: ContactsApiResponse) {
        apiResponse = response
        if (isRefreshing) {
            isRefreshing = false
            items.clear()
            items.addAll(response.data)
            if (items.isEmpty()) {
                callback.onNoRecordsFound(items)
            } else {
                callback.onItemsRefreshed(items)
            }
        } else {
            when (pageOffset) {
                1 -> {
                    items.clear()
                    items.addAll(response.data)
                    if (items.isEmpty()) {
                        callback.onNoRecordsFound(items)
                    } else {
                        callback.onInitialItemsAdded(items)
                    }
                }
                else -> {
                    callback.hideLoadMoreProgress()
                    items.addAll(response.data)
                    callback.onMoreItemsAdded(response.data.toCollection(ArrayList()))
                }
            }
        }
    }

    override fun onServerError(errorMessage: String) {
        if (isRefreshing) {
            isRefreshing = false
            callback.onRefreshServerError(errorMessage)
        } else {
            when (pageOffset) {
                1 -> {
                    callback.onInitialRequestServerError(errorMessage)
                }
                else -> {
                    pageOffset--
                    callback.onLoadMoreServerError(errorMessage)
                }
            }
        }
    }

    override fun onNetworkError() {
        if (isRefreshing) {
            isRefreshing = false
            callback.onRefreshNetworkError()
        } else {
            when (pageOffset) {
                1 -> {
                    callback.onInitialRequestNetworkError()
                }
                else -> {
                    pageOffset--
                    callback.onLoadMoreNetworkError()
                }
            }
        }
    }

    override fun getContacts(): ArrayList<Contact> {
        return items
    }

    override fun getItemsCount(): Int {
        return items.size
    }

    override fun cancelRequest() {
        apiRequest?.cancel()
    }

    override fun isLoadMoreContacts(): Boolean {
        return getItemsCount() < apiResponse?.total ?: 0
    }

    override fun saveContact(contact: Contact) {
        databaseManager.saveContact(contact = contact)
    }

    override fun deleteContact(contact: Contact) {
        databaseManager.deleteContact(contact = contact)
    }

}