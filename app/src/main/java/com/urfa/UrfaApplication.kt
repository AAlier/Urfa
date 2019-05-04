package com.urfa

import android.app.Application
import com.urfa.di.appModules
import org.koin.android.ext.android.startKoin

class UrfaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpKoin()
    }

    private fun setUpKoin() = startKoin(this, appModules)
}
