package com.rajeshjadav.android.mvputilityapp.ui.contacts

import com.rajeshjadav.android.mvputilityapp.data.database.DatabaseManager
import com.rajeshjadav.android.mvputilityapp.data.network.ApiManager

class ContactsPresenter(
    private var view: ContactsContract.View?,
    apiManager: ApiManager,
    databaseManager: DatabaseManager
) :
    ContactsContract.Presenter, ContactsRepositoryCallback {

    private val repository: ContactsContract.Repository

    init {
        repository = ContactsRepository(apiManager, databaseManager)
        repository.setListener(this)
    }

    override fun start() {
        view?.setupRecyclerView()
        view?.setupSwipeRefreshLayout()
    }

    override fun loadInitialContacts() {
        when (repository.getItemsCount()) {
            0 -> view?.showProgress()
            else -> view?.showRefreshIndicator()
        }
        repository.loadInitialContacts()
    }

    override fun loadMoreContacts() {
        if (!repository.isLoadMoreContacts()) return
        view?.showLoadMoreProgress()
        repository.loadMoreContacts()
    }

    override fun refreshContacts() {
        view?.showRefreshIndicator()
        repository.refreshContacts()
    }

    override fun onInitialItemsAdded(contacts: ArrayList<Contact>) {
        view?.resetScrollListener()
        view?.showInitialContacts(contacts)
        view?.showContent()
    }

    override fun onNoRecordsFound(contacts: ArrayList<Contact>) {
        view?.resetScrollListener()
        view?.hideRefreshIndicator()
        view?.showNoRecords()
    }

    override fun onMoreItemsAdded(contacts: ArrayList<Contact>) {
        view?.showMoreContacts(contacts.size)
    }

    override fun hideLoadMoreProgress() {
        view?.hideLoadMoreProgress()
    }

    override fun onItemsRefreshed(results: ArrayList<Contact>) {
        view?.hideRefreshIndicator()
        view?.resetScrollListener()
        view?.showInitialContacts(results)
    }

    override fun onInitialRequestServerError(errorMessage: String) {
        view?.showServerErrorState(errorMessage)
    }

    override fun onInitialRequestNetworkError() {
        view?.showNoInternetState()
    }

    override fun onLoadMoreServerError(errorMessage: String) {
        view?.showServerErrorSnackbar(errorMessage)
    }

    override fun onLoadMoreNetworkError() {
        view?.showNoInternetSnackbar()
    }

    override fun onRefreshServerError(errorMessage: String) {
        view?.hideRefreshIndicator()
        view?.showServerErrorSnackbar(errorMessage)
    }

    override fun onRefreshNetworkError() {
        view?.hideRefreshIndicator()
        view?.showNoInternetSnackbar()
    }

    override fun onItemClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFavouriteButtonClick(position: Int) {
        val contact = repository.getContacts()[position]
        if (contact.isFavourite) {
            repository.deleteContact(contact)
            contact.isFavourite = false
        } else {
            repository.saveContact(contact)
            contact.isFavourite = true
        }
        view?.updateContact(position)
    }


    override fun unbindView() {
        repository.cancelRequest()
        view = null
    }
}