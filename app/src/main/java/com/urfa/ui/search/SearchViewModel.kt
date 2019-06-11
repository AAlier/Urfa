package com.urfa.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.urfa.db.AppDatabase
import com.urfa.ui.base.BaseViewModel
import com.urfa.ui.weekview.WeekViewEvent

class SearchViewModel(database: AppDatabase) : BaseViewModel<SearchNavigation>() {
    private val filterLiveData = MutableLiveData<String>()
    val users: LiveData<PagedList<WeekViewEvent>> = Transformations.switchMap(
        filterLiveData
    ) { query ->
        database.getUserDao().findUserByNameDataSource(query)
            .toLiveData(
                Config(
                    pageSize = 30,
                    enablePlaceholders = true,
                    maxSize = 100
                )
            )
    }

    init {
        filterLiveData.postValue(null)
    }

    fun searchFor(newText: String?) {
        filterLiveData.postValue(newText)
    }
}