package com.example.lutongph.presentation.user_manual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lutongph.R

class ViewPagerAdapter(private var desc: List<String>, private var images: List<Int>) :
    RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {
    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemDesc: TextView = itemView.findViewById(R.id.desc)
        val itemImg: ImageView = itemView.findViewById(R.id.imgslider)

        init {
            itemImg.setOnClickListener { v: View ->
                val position = adapterPosition
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        return Pager2ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.slider_content, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.itemDesc.text = desc[position]
        holder.itemImg.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return desc.size
    }
}