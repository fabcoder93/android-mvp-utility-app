package com.rajeshjadav.android.mvputilityapp.common

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable

class AutoCompleteAdapter(
    context: Context,
    resourceId: Int
) : ArrayAdapter<String>(context, resourceId), Filterable {

    var autoCompleteResultItems = ArrayList<String>()

    override fun getCount(): Int {
        return autoCompleteResultItems.size
    }

    override fun getItem(position: Int): String {
        return autoCompleteResultItems[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val queryResults: ArrayList<String> = ArrayList()
                // Modify logic here to call autocomplete API
                /*if (constraint != null && constraint.isNotEmpty()) {
                    presenter.getAutoCompleteResult(constraint.toString().trim())
                } else {
                    ArrayList()
                }*/
                filterResults.values = queryResults
                filterResults.count = queryResults.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.let {
                    autoCompleteResultItems = if (it.values == null) {
                        ArrayList()
                    } else {
                        it.values as ArrayList<String>
                    }
                    if (it.count > 0) {
                        notifyDataSetChanged()
                    } else {
                        notifyDataSetInvalidated()
                    }
                }
            }
        }
    }
}
