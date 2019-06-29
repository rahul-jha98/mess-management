package com.rahul.messmanagement.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.messmanagement.R
import com.rahul.messmanagement.data.database.MenuEntry
import kotlinx.android.synthetic.main.list_item_menu.view.*

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){

    private val menuList = ArrayList<MenuEntry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_menu, parent, false))
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuList[position])
    }

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val timeSlotTextView = itemView.findViewById<TextView>(R.id.timeSlotTextView)
        val mainMenuTextView = itemView.findViewById<TextView>(R.id.mainMenuTextView)
        val dailyTextView = itemView.findViewById<TextView>(R.id.dailyTextView)

        fun bind(menuEntry: MenuEntry) {

            val slot = when(menuEntry.timeSlot) {
                1 -> "Breakfast"
                2 -> "Lunch"
                else -> "Dinner"
            }
            timeSlotTextView.text = slot
            mainMenuTextView.text = menuEntry.menu
            dailyTextView.text = menuEntry.menuDaily
        }
    }

    fun swapList(list: List<MenuEntry>) {
        menuList.clear()
        menuList.addAll(list)
        notifyDataSetChanged()
    }
}