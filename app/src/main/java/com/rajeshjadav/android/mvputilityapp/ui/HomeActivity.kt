package com.rajeshjadav.android.mvputilityapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.rajeshjadav.android.mvputilityapp.R
import com.rajeshjadav.android.mvputilityapp.ui.contacts.ContactsFragment
import com.rajeshjadav.android.mvputilityapp.ui.dashboard.DashboardFragment
import com.rajeshjadav.android.mvputilityapp.util.Events
import com.rajeshjadav.android.mvputilityapp.util.REQUEST_CODE_CHECK_LOCATION_SETTINGS
import com.rajeshjadav.android.mvputilityapp.util.extensions.addFragmentToActivity
import com.rajeshjadav.android.mvputilityapp.util.extensions.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.EventBus

class HomeActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragmentInActivity(ContactsFragment.newInstance(), R.id.frameLayout)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    replaceFragmentInActivity(DashboardFragment.newInstance(), R.id.frameLayout)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        replaceFragmentInActivity(ContactsFragment.newInstance(), R.id.frameLayout)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CHECK_LOCATION_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        EventBus.getDefault().post(Events.UserLocationUpdateEvent())
                    }
                }
            }
        }
    }
}
