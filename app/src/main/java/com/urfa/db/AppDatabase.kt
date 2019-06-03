package com.urfa.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.urfa.db.dao.UserDao
import com.urfa.ui.weekview.WeekViewEvent

@Database(
    entities = [
        WeekViewEvent::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao() : UserDao

    companion object {
        private const val DATABASE_NAME = "urfa"

        fun create(context: Context): AppDatabase =
            Room
                .databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build()
    }
}