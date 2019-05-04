package com.urfa.ui.base

interface BaseNavigation {
    fun showProgressBar()
    fun hideProgressBar()

    fun onError(error: String?)
}