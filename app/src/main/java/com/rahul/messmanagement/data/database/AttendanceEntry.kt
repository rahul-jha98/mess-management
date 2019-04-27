package com.rahul.messmanagement.data.database

import androidx.room.Entity

@Entity(tableName = "attendance", primaryKeys = ["rollNo", "currDate", "timeSlot"])
class AttendanceEntry (var rollNo : String = "",
                       var currDate : Long = 0L,
                       var timeSlot : Int = 0,
                       var isPresent: Int = 1)