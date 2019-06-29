package com.rahul.messmanagement.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun markAttendance(attendanceEntry: AttendanceEntry)

    @Query("SELECT * FROM attendance ORDER BY currDate DESC, timeSlot DESC")
    fun getAllAttendance() : LiveData<List<AttendanceEntry>>

    @Query("SELECT * FROM attendance ORDER BY currDate DESC, timeSlot DESC LIMIT 1")
    fun getLastAttendance() : LiveData<List<AttendanceEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAttendance(allAttendance : List<AttendanceEntry>)

    @Query("DELETE FROM attendance")
    fun deleteAll()
}