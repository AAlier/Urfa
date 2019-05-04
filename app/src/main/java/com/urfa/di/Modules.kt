package com.urfa.di

import com.urfa.db.AppDatabase
import com.urfa.ui.list.ListViewModel
import com.urfa.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { ListViewModel() }
}

val repositoryModule = module {
    single { AppDatabase.create(get()) }
}

val appModules = listOf(viewModelModule, repositoryModule)