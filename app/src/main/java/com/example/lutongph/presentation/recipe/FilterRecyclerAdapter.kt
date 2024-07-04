package com.example.lutongph.presentation.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lutongph.R

class FilterRecyclerAdapter : RecyclerView.Adapter<FilterRecyclerAdapter.MyViewHolder>() {
    private val mealCategoriesList =
        listOf("All", "Breakfast", "Lunch", "Dinner", "Snacks", "Beverages", "Healthy")
    private lateinit var mListener: onItemClickListener
    private var selectedPosition = 0

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val mealCategory: TextView = itemView.findViewById(R.id.meal_category)
        val container: RelativeLayout = itemView.findViewById(R.id.filter_container)

        fun bindItem(item: String, isSelected: Boolean) {
            mealCategory.text = item

            val textColor = if (isSelected) {
                R.color.white
            } else {
                R.color.themecolor_pastelwhite
            }
            mealCategory.setTextColor(ContextCompat.getColor(itemView.context, textColor))

            val buttonColor = if (isSelected) {
                R.color.themecolor_brown
            } else {
                R.color.themecolor_buttonbrown
            }
            container.setBackgroundColor(ContextCompat.getColor(itemView.context, buttonColor))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.filter_chips, parent, false)
        val viewHolder = MyViewHolder(itemView, mListener)


        itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                mListener.onItemClick(position)
                selectedPosition = position
                notifyDataSetChanged()
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = mealCategoriesList[position]
        holder.bindItem(currentItem, position == selectedPosition)
    }

    override fun getItemCount(): Int {
        return mealCategoriesList.size
    }
}
