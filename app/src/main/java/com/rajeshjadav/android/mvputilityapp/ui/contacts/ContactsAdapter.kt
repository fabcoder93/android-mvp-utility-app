package com.rajeshjadav.android.mvputilityapp.ui.contacts

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rajeshjadav.android.mvputilityapp.R
import com.rajeshjadav.android.mvputilityapp.base.BaseViewHolder
import com.rajeshjadav.android.mvputilityapp.common.ProgressViewHolder
import com.rajeshjadav.android.mvputilityapp.util.LIST_ITEM_TYPE_NORMAL
import com.rajeshjadav.android.mvputilityapp.util.LIST_ITEM_TYPE_PROGRESS
import com.rajeshjadav.android.mvputilityapp.util.extensions.inflate
import com.rajeshjadav.android.mvputilityapp.util.imageloader.GlideRequests
import com.rajeshjadav.android.mvputilityapp.util.imageloader.ImageLoader
import kotlinx.android.synthetic.main.list_item_contact.*

class ContactsAdapter(
    val imageLoader: ImageLoader,
    val glideRequests: GlideRequests?,
    val contactItemClickListener: ContactItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ArrayList<Any?>(0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LIST_ITEM_TYPE_PROGRESS -> ProgressViewHolder(parent.inflate(R.layout.list_item_progress))
            else -> ContactViewHolder(parent.inflate(R.layout.list_item_contact))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactViewHolder -> {
                holder.bind(items[position] as Contact)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()){
            when (holder) {
                is ContactViewHolder -> {
                    val contact = payloads[0] as Contact
                    holder.updateFavouriteButton(contact.isFavourite)
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            null -> LIST_ITEM_TYPE_PROGRESS
            else -> LIST_ITEM_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ContactViewHolder(override val containerView: View) :
        BaseViewHolder<Contact>(containerView) {

        private val clickListener = View.OnClickListener { view ->
            when (view?.id) {
                R.id.favouriteButton -> {
                    contactItemClickListener.onFavouriteButtonClick(adapterPosition)
                }
                else -> {
                    contactItemClickListener.onItemClick(adapterPosition)
                }
            }
        }

        init {
            itemView.setOnClickListener(clickListener)
            favouriteButton.setOnClickListener(clickListener)
        }

        override fun bind(item: Contact) = with(item) {
            nameTextView.text = "$firstName $lastName"
            emailTextView.text = email
            imageLoader.load(
                glideRequests = glideRequests,
                imageUrl = avatar,
                imageView = avatarImageView,
                doCircleCrop = true,
                placeHolder = R.drawable.ic_user_default,
                errorDrawable = R.drawable.ic_user_default
            )
        }

        fun updateFavouriteButton(isFavourite: Boolean) {
            if (isFavourite) {
                favouriteButton.setImageResource(R.drawable.ic_favourite_active)
            } else {
                favouriteButton.setImageResource(R.drawable.ic_favourite_inactive)
            }
        }

    }

    fun submitList(newItems: ArrayList<Contact>) {
        this.items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateItem(positionToUpdate: Int) {
        notifyItemChanged(positionToUpdate, items[positionToUpdate])
    }

    fun addMoreItems(newListSize: Int) {
        notifyItemRangeInserted(itemCount, newListSize)
    }

    fun showLoadMoreProgress() {
        items.add(null)
        notifyItemInserted(itemCount - 1)
    }

    fun hideLoadMoreProgress() {
        items.removeAt(itemCount - 1)
        notifyItemRemoved(itemCount)
    }

    fun removeAllItems() {
        items.clear()
        notifyDataSetChanged()
    }

}