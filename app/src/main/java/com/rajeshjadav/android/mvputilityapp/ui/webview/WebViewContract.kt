package com.rajeshjadav.android.mvputilityapp.ui.webview

import com.rajeshjadav.android.mvputilityapp.base.BasePresenter
import com.rajeshjadav.android.mvputilityapp.base.BaseView


interface WebViewContract {

    interface View : BaseView<Presenter> {

        fun setToolbar()

        fun setupWebView()

        fun showProgress(url: String)

        fun showContent()

        fun showNoInternetError()

        fun showServerError(errorMessage: String)

    }

    interface Presenter : BasePresenter {

        fun loadUrl(url: String)

    }
}