package com.urfa.ui.add

import com.urfa.ui.base.BaseNavigation
import com.urfa.ui.weekview.WeekViewEvent

interface AddUserNavigation : BaseNavigation {
    fun onSuccessSavingUser(event: WeekViewEvent)

}