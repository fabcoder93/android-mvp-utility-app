package com.rajeshjadav.android.mvputilityapp.ui.dashboard

import com.rajeshjadav.android.mvputilityapp.base.BasePresenter
import com.rajeshjadav.android.mvputilityapp.base.BaseView
import com.rajeshjadav.android.mvputilityapp.ui.contacts.Contact
import com.rajeshjadav.android.mvputilityapp.ui.contacts.ContactsRepositoryCallback

class DashboardContract {

    interface View : BaseView<Presenter> {

        fun setupGoogleMapFragment()
    }

    interface Presenter : BasePresenter {

        fun onMyLocationButtonClick()
    }

}