package com.example.lutongph.presentation.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lutongph.R
import com.example.lutongph.data.model.RecipeList
import com.squareup.picasso.Picasso


class RecipeRecyclerAdapter (var recipelist: ArrayList<RecipeList>): RecyclerView.Adapter<RecipeRecyclerAdapter.MyViewHolder>(){

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemCLick(position : Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class MyViewHolder(itemView: View, listener : onItemClickListener): RecyclerView.ViewHolder(itemView){
        val foodname: TextView = itemView.findViewById(R.id.food_name)
        val preptime: TextView = itemView.findViewById(R.id.prep_time)
        val foodprice: TextView = itemView.findViewById(R.id.food_price)
        val foodimage: ImageView = itemView.findViewById(R.id.food_image)

        init{

            itemView.setOnClickListener {
                listener.onItemCLick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_list, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = recipelist[position]
        holder.foodname.text = currentItem.food_name
        holder.preptime.text = currentItem.food_preptime
        holder.foodprice.text = currentItem.food_cost
        Picasso.get().load(currentItem.food_image).placeholder(R.drawable.ic_baseline_fastfood_24).error(
            R.drawable.ic_baseline_fastfood_24
        ).into(holder.foodimage)
    }

    override fun getItemCount(): Int {
        return recipelist.size
    }
}