package com.rahul.messmanagement.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun markAttendance(attendanceEntry: AttendanceEntry)

    @Query("SELECT * FROM attendance ORDER BY currDate, timeSlot DESC")
    fun getAllAttendance() : LiveData<List<AttendanceEntry>>

    @Query("SELECT * FROM attendance ORDER BY currDate, timeSlot DESC LIMIT 1")
    fun getLastAttendance() : LiveData<AttendanceEntry>

    @Insert
    fun saveAttendance(allAttendance : List<AttendanceEntry>)

    @Query("DELETE FROM attendance")
    fun deleteAll()
}