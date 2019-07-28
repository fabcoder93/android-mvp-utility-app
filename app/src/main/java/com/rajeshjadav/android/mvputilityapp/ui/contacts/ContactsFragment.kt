package com.rajeshjadav.android.mvputilityapp.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajeshjadav.android.mvputilityapp.R
import com.rajeshjadav.android.mvputilityapp.util.Injection
import com.rajeshjadav.android.mvputilityapp.util.LIST_ITEM_TYPE_PROGRESS
import com.rajeshjadav.android.mvputilityapp.util.RecyclerViewScrollListener
import com.rajeshjadav.android.mvputilityapp.util.extensions.inflate
import com.rajeshjadav.android.mvputilityapp.util.extensions.showNoInternetSnackbar
import com.rajeshjadav.android.mvputilityapp.util.extensions.showServerErrorSnackbar
import com.rajeshjadav.android.mvputilityapp.util.imageloader.GlideRequests
import com.rajeshjadav.android.mvputilityapp.util.imageloader.ImageLoader
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : Fragment(), ContactsContract.View {

    private lateinit var presenter: ContactsContract.Presenter
    private lateinit var tryAgainListener: View.OnClickListener
    private lateinit var scrollListener: RecyclerViewScrollListener
    private lateinit var imageLoader: ImageLoader
    private var listAdapter: ContactsAdapter? = null
    private var glideRequests: GlideRequests? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_contacts)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageLoader = Injection.provideImageLoader()
        glideRequests = Injection.provideGlideRequest(fragment = this)
        presenter = ContactsPresenter(
            view = this,
            apiManager = Injection.provideApiManager(),
            databaseManager = Injection.provideDatabaseManager()
        )
        tryAgainListener = View.OnClickListener {
            presenter.loadInitialContacts()
        }
        presenter.start()
        presenter.loadInitialContacts()
    }

    override fun setPresenter(presenter: ContactsContract.Presenter) {
        this.presenter = presenter
    }

    override fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        listAdapter = ContactsAdapter(
            imageLoader = imageLoader,
            glideRequests = glideRequests,
            contactItemClickListener = object : ContactItemClickListener {
                override fun onItemClick(position: Int) {
                    presenter.onItemClick(position)
                }

                override fun onFavouriteButtonClick(position: Int) {
                    presenter.onFavouriteButtonClick(position)
                }

            })
        recyclerView.apply {
            layoutManager = linearLayoutManager
            scrollListener = RecyclerViewScrollListener({ loadMore() }, linearLayoutManager)
            adapter = listAdapter
            clearOnScrollListeners()
            addOnScrollListener(scrollListener)
        }
    }

    private fun loadMore() {
        listAdapter?.let {
            if (it.getItemViewType(it.itemCount - 1) != LIST_ITEM_TYPE_PROGRESS) {
                recyclerView?.post {
                    if (swipeRefreshLayout?.isRefreshing == false) {
                        presenter.loadMoreContacts()
                    }
                }
            }
        }
    }

    override fun setupSwipeRefreshLayout() {
        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorPrimary)
            setOnRefreshListener {
                presenter.refreshContacts()
            }
        }
    }

    override fun showProgress() {
        progressLayout.showLoading()
    }

    override fun showInitialContacts(results: ArrayList<Contact>) {
        listAdapter?.submitList(results)
    }

    override fun showContent() {
        progressLayout.showContent()
    }

    override fun showRefreshIndicator() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideRefreshIndicator() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showLoadMoreProgress() {
        listAdapter?.showLoadMoreProgress()
    }

    override fun hideLoadMoreProgress() {
        listAdapter?.hideLoadMoreProgress()
    }

    override fun showMoreContacts(contactListSize: Int) {
        listAdapter?.addMoreItems(contactListSize)
    }

    override fun showNoRecords() {
        listAdapter?.removeAllItems()
        progressLayout.showError(
            R.drawable.ic_no_records_state,
            getString(R.string.msg_no_records_title),
            getString(R.string.msg_no_records_message),
            getString(R.string.button_try_again),
            tryAgainListener
        )
    }

    override fun showNoInternetState() {
        progressLayout.showError(
            R.drawable.ic_no_internet_state,
            getString(R.string.msg_no_internet_title),
            getString(R.string.msg_no_internet_message),
            getString(R.string.button_try_again),
            tryAgainListener
        )
    }

    override fun showServerErrorState(errorMessage: String) {
        progressLayout.showError(
            R.drawable.ic_server_error_state,
            getString(R.string.msg_server_error_title),
            getString(R.string.msg_server_error_message),
            getString(R.string.button_try_again),
            tryAgainListener
        )
    }

    override fun showNoInternetSnackbar() {
        progressLayout.showNoInternetSnackbar()
    }

    override fun showServerErrorSnackbar(errorMessage: String) {
        progressLayout.showServerErrorSnackbar(errorMessage)
    }

    override fun resetScrollListener() {
        scrollListener.resetState()
    }

    override fun updateContact(position: Int) {
        listAdapter?.updateItem(position)
    }

    override fun showContactDetails(contactId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroyView() {
        presenter.unbindView()
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): ContactsFragment {
            return ContactsFragment()
        }
    }

}