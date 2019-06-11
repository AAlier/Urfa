package com.urfa.ui.add

import androidx.lifecycle.MutableLiveData
import com.urfa.db.AppDatabase
import com.urfa.ui.base.BaseViewModel
import com.urfa.ui.weekview.WeekViewEvent
import java.util.*

class AddUserViewModel(private val database: AppDatabase) : BaseViewModel<AddUserNavigation>() {
    val newUser = MutableLiveData<WeekViewEvent>()

    init {
        val defaultUser = WeekViewEvent()
        newUser.postValue(defaultUser)
    }

    fun getBirthDayCalendar() = newUser.value?.birthDate ?: Calendar.getInstance()

    fun getStartDayCalendar() = newUser.value?.startTime ?: Calendar.getInstance()

    fun getEndDayCalendar() : Calendar {
        val date = Calendar.getInstance()
        date.add(Calendar.MINUTE, 10)
        return newUser.value?.endTime ?: date
    }

    fun saveUser() {
        if (getStartDayCalendar().compareTo(getEndDayCalendar()) != -1) {
            getNavigation()?.onError("Дата должна быть логичной")
            return
        }
        Thread {
            getNavigation()?.showProgressBar()
            newUser.value?.let {
                database.getUserDao().insert(it)
                getNavigation()?.onSuccessSavingUser(it)
                getNavigation()?.hideProgressBar()
            } ?: run {
                getNavigation()?.onError("Данные не могут быть пустыми")
                getNavigation()?.hideProgressBar()
            }
        }.start()
    }

    fun addUri(uri: String) {
        val event = newUser.value ?: WeekViewEvent()
        event.filePath = uri
        newUser.postValue(event)
    }
}