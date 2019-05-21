package com.urfa.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.urfa.db.AppDatabase
import com.urfa.ui.base.BaseViewModel
import com.urfa.ui.weekview.WeekViewEvent
import java.util.*

class MainViewModel(private val database: AppDatabase) : BaseViewModel<MainNavigation>() {
    private val filterLiveData = MutableLiveData<Calendar>()
    val users: LiveData<PagedList<WeekViewEvent>> = Transformations.switchMap(
        filterLiveData
    ) { end ->
        val start = end.clone() as Calendar
        start.add(Calendar.MONTH, -1)
        database.getUserDao().findAllGroupsDataSource()
            .toLiveData(
                Config(
                    pageSize = 30,
                    enablePlaceholders = true,
                    maxSize = 100
                )
            )
    }

    fun updateFilter(year: Int, month: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        filterLiveData.postValue(calendar)
    }
}