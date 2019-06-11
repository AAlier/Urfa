package com.urfa.di

import com.urfa.db.AppDatabase
import com.urfa.ui.add.AddUserViewModel
import com.urfa.ui.list.ListViewModel
import com.urfa.ui.main.MainViewModel
import com.urfa.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ListViewModel(get()) }
    viewModel { AddUserViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}

val repositoryModule = module {
    single { AppDatabase.create(get()) }
}

val appModules = listOf(viewModelModule, repositoryModule)