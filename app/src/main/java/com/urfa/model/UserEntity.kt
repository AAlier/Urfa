package com.urfa.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.urfa.db.dao.BaseEntity
import java.util.*

@Entity(
    tableName = "user",
    indices = [
        Index("id")
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    @ColumnInfo(name = "username")
    var firstName: String,
    @ColumnInfo(name = "surname")
    var lastName: String,
    @ColumnInfo(name = "start_date")
    var startDate: Date? = null,
    @ColumnInfo(name = "end_date")
    var endDate: Date? = null
) : BaseEntity<Long>