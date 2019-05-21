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

    fun getEndDayCalendar() = newUser.value?.endTime ?: Calendar.getInstance()

    fun saveUser() {
        if(getStartDayCalendar().compareTo(getEndDayCalendar()) != -1) {
            getNavigation()?.onError("Дата должна быть логичной")
            return
        }
        Thread {
            getNavigation()?.showProgressBar()
            newUser.value?.let {
                database.getUserDao().insert(it)
                getNavigation()?.onSuccessSavingUser()
                getNavigation()?.hideProgressBar()
            } ?: kotlin.run {
                getNavigation()?.onError("Данные не могут быть пустыми")
                getNavigation()?.hideProgressBar()
            }
        }.start()
    }
}