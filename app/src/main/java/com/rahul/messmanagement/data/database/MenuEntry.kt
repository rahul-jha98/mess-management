package com.rahul.messmanagement.data.database

import androidx.room.Entity

@Entity(tableName = "menu", primaryKeys = ["weekday", "timeSlot"])
class MenuEntry (var mess : String,
                 var weekday : Int,
                 var timeSlot: Int,
                 var menu: String,
                 var menuDaily : String)