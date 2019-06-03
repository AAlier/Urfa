package com.urfa.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.urfa.db.AppDatabase
import com.urfa.ui.base.BaseViewModel
import com.urfa.ui.weekview.WeekViewEvent
import com.urfa.util.monthTimeFormatter
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.HashMap

class ListViewModel(private val database: AppDatabase) : BaseViewModel<ListNavigation>() {
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
    val selectedIndices = MutableLiveData<CopyOnWriteArrayList<Int>>()
    private val selectedIds = HashMap<Long, WeekViewEvent>()

    init {
        selectedIndices.postValue(CopyOnWriteArrayList())
    }

    fun updateSelectedId(adapterPosition: Int, event: WeekViewEvent) {
        val selectedPos = selectedIndices.value ?: CopyOnWriteArrayList()
        if (!selectedPos.contains(adapterPosition)) {
            selectedPos.add(adapterPosition)
            selectedIds.put(event.id, event)
        } else {
            selectedPos.remove(adapterPosition)
            selectedIds.remove(event.id)
        }
        selectedIndices.postValue(selectedPos)
    }

    fun updateFilter(year: Int, month: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        filterLiveData.postValue(calendar)
    }

    fun deleteSelected() {
        Thread {
            getNavigation()?.showProgressBar()
            database.getUserDao().delete(selectedIds.keys)
            getNavigation()?.hideProgressBar()
        }.start()
    }

    fun getShareableText(): String {
        val builder = StringBuilder()
        val list = selectedIds.values.toMutableList()
        list.sortWith(Comparator { event1, event2 -> if(event1!!.startTime.timeInMillis >= event2!!.startTime.timeInMillis) 1 else -1 })
        list.forEach {
            builder.append("${monthTimeFormatter.format(it.startTime.time)} ${it.name} ${it.lastName}\n")
        }
        return builder.toString()
    }

    fun isSingular() = selectedIndices.value?.size == 1
}