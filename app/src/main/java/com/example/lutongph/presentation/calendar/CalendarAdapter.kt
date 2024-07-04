package com.example.lutongph.presentation.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lutongph.R
import java.time.LocalDate
import java.util.ArrayList

class CalendarAdapter(private val daysOfWeek: ArrayList<LocalDate>, private val onItemListener: OnItemListener) :
    RecyclerView.Adapter<CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams


        if (daysOfWeek.size > 15) {
            layoutParams.height = (parent.getHeight() * 0.12).toInt()
        } else {
            layoutParams.height = parent.getHeight()
        }

        return CalendarViewHolder(view, onItemListener, daysOfWeek)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = daysOfWeek.get(position)
        if (date == null) {
            holder.dayOfMonth.text = ""
        } else {
            holder.dayOfMonth.text = date.dayOfMonth.toString()
            if (date == Calendar.sdate) {
                holder.parentView.setBackgroundColor(Color.parseColor("#FFF1B3"))
            }
        }
    }

    override fun getItemCount(): Int {
        return daysOfWeek.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate)
    }
}
