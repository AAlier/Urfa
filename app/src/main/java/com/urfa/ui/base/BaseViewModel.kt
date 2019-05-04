package com.urfa.ui.base

import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

abstract class BaseViewModel<T : BaseNavigation> : ViewModel() {

    private lateinit var navigation: WeakReference<T>

    fun getNavigation() = navigation.get()

    fun setNavigation(navigator: T) {
        navigation = WeakReference(navigator)
    }

    fun setError(error: Throwable) {
        getNavigation()?.onError(error.message)
    }
}