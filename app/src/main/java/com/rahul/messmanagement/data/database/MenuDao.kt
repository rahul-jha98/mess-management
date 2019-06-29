package com.rahul.messmanagement.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MenuDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMenu(menuEntry: MenuEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMenu(allMenu : List<MenuEntry>)

    @Query("SELECT * FROM menu WHERE weekday = :day")
    fun getMenuForDay(day : Int) : LiveData<List<MenuEntry>>

    @Query("SELECT * FROM menu WHERE weekday = :day AND timeSlot = :timeSlot")
    fun getNextMenu(day : Int, timeSlot: Int) : LiveData<List<MenuEntry>>

    @Query("DELETE FROM menu")
    fun deleteAll()

    @Query("SELECT * FROM menu ORDER BY weekday, timeSlot")
    fun getAll(): LiveData<List<MenuEntry>>
}