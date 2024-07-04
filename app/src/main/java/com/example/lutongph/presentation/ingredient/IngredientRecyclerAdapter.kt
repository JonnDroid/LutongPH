package com.example.lutongph.presentation.ingredient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lutongph.R
import com.example.lutongph.data.model.SelectedIngredientList
import com.squareup.picasso.Picasso

class IngredientRecyclerAdapter(var inglist: ArrayList<SelectedIngredientList>): RecyclerView.Adapter<IngredientRecyclerAdapter.MyViewHolder>(){
    private lateinit var mListener : removeOnItemClickListener

    interface removeOnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setremoveOnItemClickListener(listener: removeOnItemClickListener){
        mListener = listener
    }

    class MyViewHolder (itemView: View, listener : removeOnItemClickListener) : RecyclerView.ViewHolder(itemView) {
            val ing_image: ImageView = itemView.findViewById(R.id.ingredient_image)
            val ing_name: TextView = itemView.findViewById(R.id.ingredient_name)
            val deleteBtn : Button = itemView.findViewById(R.id.remove_button)

        init{
            deleteBtn.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ingredients_layout, parent, false)
        return MyViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = inglist[position]
        holder.ing_name.text = currentItem.ingredient_name
        Picasso.get().load(currentItem.ingredient_image).placeholder(R.drawable.ic_baseline_fastfood_24).error(
            R.drawable.ic_baseline_fastfood_24
        ).into(holder.ing_image)
    }

    override fun getItemCount(): Int {
        return inglist.size
    }


}