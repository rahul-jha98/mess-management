package com.rahul.messmanagement.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rahul.messmanagement.MessApplication
import com.rahul.messmanagement.R
import com.rahul.messmanagement.adapters.AttendanceAdapter
import com.rahul.messmanagement.viewmodels.AttendanceViewModel
import com.rahul.messmanagement.viewmodels.RoomViewModelFactory
import kotlinx.android.synthetic.main.activity_attendance.*

class AttendanceActivity : AppCompatActivity() {
    private lateinit var roomViewModelFactory: RoomViewModelFactory
    private lateinit var attendanceAdapter: AttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Your Attendance Record"

        roomViewModelFactory = (application as MessApplication).appComponent.getViewModelFactory()
        val viewModel = ViewModelProviders.of(this , roomViewModelFactory).get(AttendanceViewModel::class.java)

        attendanceAdapter = AttendanceAdapter()

        attendanceRv.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = attendanceAdapter
        }

        viewModel.attendanceList.observe(this, Observer {
            it?.let { list ->
                attendanceAdapter.swapList(list)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            return when(item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                } else -> super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
