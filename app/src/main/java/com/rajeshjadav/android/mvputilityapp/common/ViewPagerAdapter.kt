package com.rajeshjadav.android.mvputilityapp.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(
    fragmentManager: FragmentManager?,
    private val fragments: ArrayList<Fragment>
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}
