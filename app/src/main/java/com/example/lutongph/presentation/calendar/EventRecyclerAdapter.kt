package com.example.lutongph.presentation.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lutongph.R
import com.example.lutongph.data.model.EventList

class EventRecyclerAdapter(var eventList: ArrayList<EventList>) : RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder>() {
    private lateinit var mListener: RemoveOnItemClickListener

    interface RemoveOnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setRemoveOnItemClickListener(listener: RemoveOnItemClickListener) {
        mListener = listener
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventTime: TextView = itemView.findViewById(R.id.eventTime)
        val eventTitle: TextView = itemView.findViewById(R.id.eventTitle)
        val eventFood: TextView = itemView.findViewById(R.id.eventFood)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_list, parent, false)
        val viewHolder = MyViewHolder(itemView)
        val position = viewHolder.adapterPosition
        viewHolder.deleteButton.setOnClickListener {
            mListener.onItemClick(position)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = eventList[position]
        holder.eventTime.text = currentItem.event_time
        holder.eventTitle.text = currentItem.event_title
        holder.eventFood.text = currentItem.event_food

        holder.deleteButton.setOnClickListener {
            mListener.onItemClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}


