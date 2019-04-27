package com.rahul.messmanagement.viewmodels

import androidx.lifecycle.ViewModel
import com.rahul.messmanagement.data.DataRepository

class AttendanceViewModel(repository: DataRepository) : ViewModel() {
    var attendanceList = repository.getAllAttendance();
}