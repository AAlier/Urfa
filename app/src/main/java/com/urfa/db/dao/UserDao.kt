package com.urfa.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.urfa.ui.weekview.WeekViewEvent

@Dao
interface UserDao : EntityWithAutoincrementDao<WeekViewEvent> {

    @Query("SELECT * FROM user")
    fun findAllGroupsDataSource(
    ): DataSource.Factory<Int, WeekViewEvent>

    @Query("UPDATE user SET filePath = :file WHERE id = :id")
    fun updateUserFile(id: Int, file: String);

    @Query("DELETE FROM user WHERE id IN (:list)")
    fun delete(list: Set<Long>)
}