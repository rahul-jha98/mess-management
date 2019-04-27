package com.rahul.messmanagement.adapters

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rahul.messmanagement.R
import com.rahul.messmanagement.data.network.networkmodels.RatingRequest
import java.util.*

class RatingAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder> () {

    private val feedbackeList = ArrayList<RatingRequest>()

    private val TYPE_COMPLAINT = 0
    private val TYPE_NO_COMPLAINT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_COMPLAINT -> RatingWithComplaintViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_rating_complaint, parent, false))
            else -> RatingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_rating, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return feedbackeList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is RatingViewHolder -> holder.bind(feedbackeList[position])
            is RatingWithComplaintViewHolder -> holder.bind(feedbackeList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(feedbackeList[position].complaint.isNullOrBlank()) {
            return TYPE_NO_COMPLAINT
        }
        return TYPE_COMPLAINT
    }

    private val colors = arrayOf(ContextCompat.getColor(context, R.color.redBad), ContextCompat.getColor(context, R.color.orangeBad),
        ContextCompat.getColor(context, R.color.yellowNormal), ContextCompat.getColor(context, R.color.greenOk),
        ContextCompat.getColor(context, R.color.greenGood))


    inner class RatingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeTextView = itemView.findViewById<TextView>(R.id.timeSlotTextView)
        private val dateView = itemView.findViewById<TextView>(R.id.dateTextView)
        private val ratingTextView = itemView.findViewById<TextView>(R.id.ratingTextView)
        private val starImageView = itemView.findViewById<ImageView>(R.id.startImageView)

        fun bind(ratingRequest: RatingRequest) {
            val slot = when(ratingRequest.timeSlot) {
                1 -> "Breakfast"
                2 -> "Lunch"
                else -> "Dinner"
            }

            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = ratingRequest.currDate
            val date = DateFormat.format("dd, MMM", cal).toString()

            timeTextView.text = slot
            dateView.text = date
            ratingTextView.text = ratingRequest.rating.toString()

            ratingTextView.setTextColor(colors[ratingRequest.rating - 1])
            starImageView.setColorFilter(colors[ratingRequest.rating - 1])
        }
    }

    inner class RatingWithComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeTextView = itemView.findViewById<TextView>(R.id.timeSlotTextView)
        private val dateView = itemView.findViewById<TextView>(R.id.dateTextView)
        private val ratingTextView = itemView.findViewById<TextView>(R.id.ratingTextView)
        private val starImageView = itemView.findViewById<ImageView>(R.id.startImageView)
        private val complaintTextView = itemView.findViewById<TextView>(R.id.complaintTextView)

        fun bind(ratingRequest: RatingRequest) {
            val slot = when(ratingRequest.timeSlot) {
                1 -> "Breakfast"
                2 -> "Lunch"
                else -> "Dinner"
            }

            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = ratingRequest.currDate
            val date = DateFormat.format("dd, MMM", cal).toString()

            timeTextView.text = slot
            dateView.text = date
            ratingTextView.text = ratingRequest.rating.toString()

            ratingTextView.setTextColor(colors[ratingRequest.rating - 1])
            starImageView.setColorFilter(colors[ratingRequest.rating - 1])

            complaintTextView.text = ratingRequest.complaint
        }
    }

    fun swapList(list: List<RatingRequest>) {
        feedbackeList.clear()
        feedbackeList.addAll(list)
        notifyDataSetChanged()
    }
}