package com.rajeshjadav.android.mvputilityapp.ui.webview

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.rajeshjadav.android.mvputilityapp.R
import com.rajeshjadav.android.mvputilityapp.util.ERROR_MESSAGE_SERVER_SIDE
import com.rajeshjadav.android.mvputilityapp.util.extensions.isOnline
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity(), WebViewContract.View {

    private lateinit var presenter: WebViewContract.Presenter
    private lateinit var tryAgainListener: View.OnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        tryAgainListener = View.OnClickListener {
            presenter.loadUrl("https://www.google.com")
        }
        presenter = WebViewPresenter(this)
        presenter.start()
        presenter.loadUrl("https://www.google.com")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        this.webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        this.webView.restoreState(savedInstanceState)
    }

    override fun setPresenter(presenter: WebViewContract.Presenter) {
        this.presenter = presenter
    }

    override fun setToolbar() {
//        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Webview"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.isVerticalScrollBarEnabled = false
        webView.webViewClient = object : WebViewClient() {

            @SuppressWarnings("deprecation")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return handleUri(Uri.parse(url))
            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return handleUri(request?.url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                showContent()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                showServerError(ERROR_MESSAGE_SERVER_SIDE)
            }

        }
        webView.webChromeClient = WebChromeClient()
    }

    private fun handleUri(uri: Uri?): Boolean {
        if (uri?.scheme?.contains("market") == true) {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
            return true
        } else {
            return false
        }
    }

    override fun showProgress(url: String) {
        progressLayout.showLoading()
        if (isOnline()) {
            webView.loadUrl(url)
        } else {
            showNoInternetError()
        }
    }

    override fun showContent() {
        progressLayout.showContent()
    }

    override fun showNoInternetError() {
        progressLayout.showError(
            R.drawable.ic_no_internet_state,
            getString(R.string.msg_no_internet_title),
            getString(R.string.msg_no_internet_message),
            getString(R.string.button_try_again),
            tryAgainListener
        )
    }

    override fun showServerError(errorMessage: String) {
        progressLayout.showError(
            R.drawable.ic_server_error_state,
            getString(R.string.msg_server_error_title),
            getString(R.string.msg_server_error_message),
            getString(R.string.button_try_again),
            tryAgainListener
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        presenter.unbindView()
        super.onDestroy()
    }
}
