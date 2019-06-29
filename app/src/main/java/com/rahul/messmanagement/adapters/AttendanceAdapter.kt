package com.rahul.messmanagement.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.messmanagement.R
import com.rahul.messmanagement.data.database.AttendanceEntry
import java.util.*


class AttendanceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> () {

    private val attendanceList = ArrayList<AttendanceEntry>()

    private val TYPE_PRESENT = 0
    private val TYPE_ABSENT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = when(viewType) {
            TYPE_PRESENT -> LayoutInflater.from(parent.context).inflate(R.layout.list_item_attendance, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.list_item_attendnace_absent, parent, false)
        }
        return PresentViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when (attendanceList[position].isPresent) {
            0 -> TYPE_ABSENT
            else -> TYPE_PRESENT
        }
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PresentViewHolder).bind(attendanceList[position])
    }


    inner class PresentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)
        private val timeSlotTextView = itemView.findViewById<TextView>(R.id.timeSlotTextView)
        fun bind(attendanceEntry: AttendanceEntry) {
            val slot = when(attendanceEntry.timeSlot) {
                1 -> "Breakfast"
                2 -> "Lunch"
                else -> "Dinner"
            }

            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = attendanceEntry.currDate
            val date = DateFormat.format("dd, MMM", cal).toString()

            dateTextView.text = date
            timeSlotTextView.text = slot
        }
    }

    fun swapList(list: List<AttendanceEntry>) {
        attendanceList.clear()
        attendanceList.addAll(list)
        notifyDataSetChanged()
    }
}