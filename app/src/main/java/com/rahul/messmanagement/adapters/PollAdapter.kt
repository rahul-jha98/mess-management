package com.rahul.messmanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rahul.messmanagement.R
import com.rahul.messmanagement.data.network.networkmodels.PollQuestion
import com.rahul.messmanagement.data.network.networkmodels.RebateRequest
import kotlinx.android.synthetic.main.list_item_poll.view.*

class PollAdapter(private val context: Context, private val itemClick : (PollQuestion) -> Unit) : RecyclerView.Adapter<PollAdapter.PollViewHolder>(){

    private val pollList = ArrayList<PollQuestion>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollViewHolder {
        return PollViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_poll, parent, false), itemClick)
    }

    override fun getItemCount(): Int {
        return pollList.size
    }

    override fun onBindViewHolder(holder: PollViewHolder, position: Int) {
        holder.bind(pollList[position])
    }


    inner class PollViewHolder(itemView : View, private val itemClick: (PollQuestion) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val liveTextView = itemView.findViewById<TextView>(R.id.liveTextView)
        private val questionTextView = itemView.findViewById<TextView>(R.id.pollQuestionTextView)

        fun bind(pollQuestion: PollQuestion) {
            liveTextView.visibility = when(pollQuestion.isLive) {
                1 -> View.VISIBLE
                else -> View.GONE
            }

            questionTextView.text = pollQuestion.question

            when(pollQuestion.isLive) {
                1 -> {
                    itemView.setOnClickListener {
                        itemClick(pollQuestion)
                    }
                }
                else -> {
                    itemView.setOnClickListener {
                        Toast.makeText(context, "The poll is not live", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun swapList(list: List<PollQuestion>) {
        pollList.clear()
        pollList.addAll(list)
        notifyDataSetChanged()
    }
}