package com.example.lutongph.presentation.calendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lutongph.R
import java.time.LocalDate

class CalendarViewHolder(itemView: View, private val onItemListener: CalendarAdapter.OnItemListener, var daysOfWeek: ArrayList<LocalDate>) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var parentView: View = itemView.findViewById(R.id.parentView)
    var dayOfMonth = itemView.findViewById<TextView>(R.id.days_cell)

    init {
        itemView.setOnClickListener(this)
        this.daysOfWeek = daysOfWeek
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, daysOfWeek[adapterPosition])
    }

}
