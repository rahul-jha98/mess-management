package com.rahul.messmanagement.adapters

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rahul.messmanagement.R
import com.rahul.messmanagement.data.network.networkmodels.RebateRequest
import java.util.*

class RebateAdapter(val context: Context) : RecyclerView.Adapter<RebateAdapter.RebateViewHolder> ()  {

    private val rebateList = ArrayList<RebateRequest>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RebateViewHolder {
        return RebateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_rebate, parent, false))
    }

    override fun getItemCount(): Int {
        return rebateList.size
    }

    override fun onBindViewHolder(holder: RebateViewHolder, position: Int) {
        holder.bind(rebateList[position])
    }

    private val colors = arrayOf(
        ContextCompat.getColor(context, R.color.redBad),
        ContextCompat.getColor(context, R.color.yellowNormal),
        ContextCompat.getColor(context, R.color.greenGood))

    private val statusText = arrayOf("Rejected", "Submitted", "Accepted")
    inner class RebateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fromTextView = itemView.findViewById<TextView>(R.id.fromTextView)
        private val toTextView = itemView.findViewById<TextView>(R.id.toTextView)
        private val statusButton = itemView.findViewById<Button>(R.id.statusButton)

        fun bind(rebateRequest: RebateRequest) {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = rebateRequest.fromDate
            val fromDate = DateFormat.format("dd, MMM", cal).toString()

            cal.timeInMillis = rebateRequest.toDate
            val toDate = DateFormat.format("dd, MMM", cal).toString()

            fromTextView.text = fromDate
            toTextView.text = toDate

            statusButton.setTextColor(colors[rebateRequest.status + 1])
            statusButton.text = statusText[rebateRequest.status + 1]
        }
    }

    fun swapList(list: List<RebateRequest>) {
        rebateList.clear()
        rebateList.addAll(list)
        notifyDataSetChanged()
    }
}