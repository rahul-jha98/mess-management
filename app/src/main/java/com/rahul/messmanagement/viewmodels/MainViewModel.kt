package com.rahul.messmanagement.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.database.AttendanceEntry
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel(repository: DataRepository) : ViewModel() {
    val lastEntry = repository.getLastEntry()
}