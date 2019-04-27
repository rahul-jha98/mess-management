package com.rahul.messmanagement.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(AttendanceEntry::class)], version = 1, exportSchema = false)
abstract class MessDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "messDb"
        private val LOCK = Object()

        @Volatile
        var databaseInstance : MessDatabase? = null

        fun getInstance(context: Context) : MessDatabase {
            if(databaseInstance == null) {
                synchronized(LOCK) {
                    if(databaseInstance == null) {
                        databaseInstance = Room.databaseBuilder(context.applicationContext,
                            MessDatabase::class.java,
                            MessDatabase.DATABASE_NAME).build()
                    }
                }
            }
            return databaseInstance!!
        }
    }

    abstract fun attendanceDao() : AttendanceDao
}