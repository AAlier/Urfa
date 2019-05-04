package com.urfa.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.urfa.model.UserEntity
import java.util.*

@Dao
interface UserDao : EntityWithAutoincrementDao<UserEntity> {

    @Query("SELECT * FROM user WHERE schedule BETWEEN :startDate AND :endDate")
    fun findAllGroupsDataSource(startDate: Date, endDate: Date): DataSource.Factory<Int, UserEntity>
}