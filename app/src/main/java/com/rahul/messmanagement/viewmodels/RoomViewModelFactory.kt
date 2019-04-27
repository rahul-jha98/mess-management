package com.rahul.messmanagement.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rahul.messmanagement.data.DataRepository


class RoomViewModelFactory constructor(private val repository: DataRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AttendanceViewModel::class.java) -> AttendanceViewModel(this.repository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(this.repository) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}