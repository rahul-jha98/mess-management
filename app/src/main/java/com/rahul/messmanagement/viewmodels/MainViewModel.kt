package com.rahul.messmanagement.viewmodels


import androidx.lifecycle.ViewModel
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.utils.User


class MainViewModel(val repository: DataRepository) : ViewModel() {
    val lastEntry = repository.getLastEntry()

    var nextTimeTable = repository.getAllMenu(User.timeSlot, User.dayOfWeek)
}