package com.urfa.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.urfa.ui.weekview.WeekViewEvent

@Dao
interface UserDao : EntityWithAutoincrementDao<WeekViewEvent> {

    @Query("SELECT * FROM user")
    fun findAllGroupsDataSource(
    ): DataSource.Factory<Int, WeekViewEvent>
}