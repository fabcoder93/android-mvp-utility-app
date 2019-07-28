package com.rajeshjadav.android.mvputilityapp.base

interface BaseView<in T> {
    fun setPresenter(presenter: T)
}
