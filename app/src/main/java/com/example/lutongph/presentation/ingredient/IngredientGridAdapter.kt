package com.example.lutongph.presentation.ingredient

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.lutongph.R
import com.example.lutongph.data.model.FilteredFoodList
import com.squareup.picasso.Picasso

class IngredientGridAdapter(val context: Context, val foodlist: ArrayList<FilteredFoodList>) : BaseAdapter(){
    override fun getCount(): Int {
        return foodlist.size
    }

    override fun getItem(position: Int): Any {
        return foodlist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.food_gridlayout, parent, false)
        val item = foodlist[position]

        val foodname : TextView = view.findViewById(R.id.selected_name)
        val foodimage : ImageView = view.findViewById(R.id.selected_image)
        foodname.text = item.food_name
        Picasso.get().load(item.food_image).placeholder(R.drawable.ic_baseline_fastfood_24).error(R.drawable.ic_baseline_fastfood_24).into(foodimage)

        return view
    }

}