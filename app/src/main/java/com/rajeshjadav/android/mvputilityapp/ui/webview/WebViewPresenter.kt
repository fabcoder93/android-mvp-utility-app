package com.rajeshjadav.android.mvputilityapp.ui.webview


class WebViewPresenter(
    private var view: WebViewContract.View?
) : WebViewContract.Presenter {

    override fun start() {
        view?.setToolbar()
        view?.setupWebView()
    }

    override fun loadUrl(url: String) {
        view?.showProgress(url)
    }

    override fun unbindView() {
        view = null
    }
}