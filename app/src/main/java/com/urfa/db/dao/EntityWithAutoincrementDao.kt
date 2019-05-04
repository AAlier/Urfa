package com.urfa.db.dao


import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE

/**
 * Base DAO for entity with autoincrement Id (Long)
 */
interface EntityWithAutoincrementDao<T : BaseEntity<Long>> : BaseDao<T> {

    @Insert
    fun insertAndReturnId(entity: T): Long

    @Insert
    fun insertAndReturnIds(entities: List<T>): Array<Long>

    /**
     * @return Id if inserted or -1 if some conflict occurs
     */
    @Insert(onConflict = IGNORE)
    fun tryInsertAndReturnId(entity: T): Long

    /**
     * @return Id if inserted or -1 if some conflict occurs
     */
    @Insert(onConflict = IGNORE)
    fun tryInsertAndReturnIds(entities: List<T>): Array<Long>
}