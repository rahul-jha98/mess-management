package com.rahul.messmanagement.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.database.MenuEntry

class MenuViewModel(repository: DataRepository) : ViewModel() {
    var menuList : Array<LiveData<List<MenuEntry>>> = Array(7) {
        if(it == 0) {
            repository.getMenuForDay(7)
        } else {
            repository.getMenuForDay(it)
        }
    }
}