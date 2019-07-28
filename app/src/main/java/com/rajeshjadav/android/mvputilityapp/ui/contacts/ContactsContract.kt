package com.rajeshjadav.android.mvputilityapp.ui.contacts

import com.rajeshjadav.android.mvputilityapp.base.BasePresenter
import com.rajeshjadav.android.mvputilityapp.base.BaseView

class ContactsContract {

    interface View : BaseView<Presenter> {

        fun setupRecyclerView()

        fun setupSwipeRefreshLayout()

        fun showInitialContacts(results: ArrayList<Contact>)

        fun showProgress()

        fun showContent()

        fun showRefreshIndicator()

        fun hideRefreshIndicator()

        fun showLoadMoreProgress()

        fun hideLoadMoreProgress()

        fun showMoreContacts(contactListSize: Int)

        fun resetScrollListener()

        fun showNoRecords()

        fun showNoInternetState()

        fun showServerErrorState(errorMessage: String)

        fun showNoInternetSnackbar()

        fun showServerErrorSnackbar(errorMessage: String)

        fun updateContact(position: Int)

        fun showContactDetails(contactId: Int)
    }

    interface Presenter : BasePresenter {

        fun loadInitialContacts()

        fun loadMoreContacts()

        fun refreshContacts()

        fun onItemClick(position: Int)

        fun onFavouriteButtonClick(position: Int)
    }

    interface Repository {

        fun setListener(listener: ContactsRepositoryCallback)

        fun loadInitialContacts()

        fun loadMoreContacts()

        fun refreshContacts()

        fun getContacts(): ArrayList<Contact>

        fun getItemsCount(): Int

        fun cancelRequest()

        fun isLoadMoreContacts(): Boolean

        fun saveContact(contact: Contact)

        fun deleteContact(contact: Contact)
    }


}