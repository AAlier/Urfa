package com.urfa.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base DAO for entity
 */
interface BaseDao<T : BaseEntity<Long>> {

    @Delete
    fun delete(entity: T)

    @Update
    fun update(entity: T)

    @Update
    fun update(entity: List<T>)

    @Insert
    fun insert(entity: T)

    @Insert
    fun insert(entities: List<T>)

    /**
     * @return Id if inserted or -1 if some conflict occurs
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun tryInsert(entity: T)

    /**
     * @return Id if inserted or -1 if some conflict occurs
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun tryInsert(entities: List<T>)
}