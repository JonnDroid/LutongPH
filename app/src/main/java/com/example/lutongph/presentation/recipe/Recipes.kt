package com.example.lutongph.presentation.recipe

import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lutongph.R
import com.example.lutongph.presentation.recipe_item.RecipeItem
import com.example.lutongph.data.model.RecipeList
import com.example.lutongph.util.DBHelper


class Recipes : Fragment() {

    private lateinit var  recyclerView: RecyclerView
    private lateinit var  filterChips: RecyclerView

    private  lateinit var adapter: RecipeRecyclerAdapter
    private  lateinit var fadapter: FilterRecyclerAdapter
    private lateinit var  db: DBHelper
    private lateinit var array: ArrayList<RecipeList>
    private lateinit var filtered_array: ArrayList<RecipeList>
    private lateinit var reference_array: ArrayList<RecipeList>
    private lateinit var search : AutoCompleteTextView

    private lateinit var idList : MutableList<Int>
    var searchText : String = ""
    var query : String = ""
    var category : String = "All"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        var view: View =inflater.inflate(R.layout.fragment_recipes,container,false)
        filtered_array = ArrayList()
        reference_array = ArrayList()


        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.setHasFixedSize(true)

        val layoutMngr = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterChips = view.findViewById(R.id.filter_holder)
        filterChips.layoutManager = layoutMngr
        filterChips.setHasFixedSize(true)
        fadapter = FilterRecyclerAdapter()
        filterChips.adapter = fadapter

        db = DBHelper(view.context)
        display()

        search = view.findViewById(R.id.search_view)

        search.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()){
                    filter(s.toString())
                    searchText = s.toString()

                }else{
                    filtered_array.clear()
                    filtered_array.addAll(reference_array)
                    recyclerView.adapter!!.notifyDataSetChanged()
                    searchText=""

                }
            }
        })

        fadapter.setOnItemClickListener(object: FilterRecyclerAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                when(position){
                    0 -> {
                        search.setText("")
                        category="All"
                        getID()
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    1 -> {
                        search.setText("")
                        category="Breakfast"
                        getID()
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    2 -> {
                        search.setText("")
                        category="Lunch"
                        getID()
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    3 -> {
                        search.setText("")
                        category="Dinner"
                        getID()
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    4 -> {
                        search.setText("")
                        category="Snacks"
                        getID()
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    5 -> {
                        search.setText("")
                        category="Beverages"
                        getID()
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    6 -> {
                        search.setText("")
                        category="Healthy"
                        getID()
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }
            }

        })

            return view
    }

    private fun display(){

        var newcursor: Cursor? = db!!.getrecipe()
        array = ArrayList()
        while (newcursor!!.moveToNext()){
            val foodid = newcursor.getInt(0)
            val foodname = newcursor.getString(1)
            val foodprocedure = newcursor.getString(2)
            val preptime = newcursor.getString(3)
            val foodingredients = newcursor.getString(4)
            val foodcost = newcursor.getString(5)
            val fooddesc = newcursor.getString(6)
            val foodimage = "https://drive.google.com/uc?id=" + newcursor.getString(7)
            array.add(RecipeList(foodid, foodname, foodprocedure, preptime, foodingredients, foodcost, fooddesc, foodimage ))
        }
        newcursor.close()
        filtered_array.addAll(array)
        reference_array.addAll(array)
        adapter = RecipeRecyclerAdapter(filtered_array)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object: RecipeRecyclerAdapter.onItemClickListener {
            override fun onItemCLick(position: Int) {
                getID()
                val itemposition = idList[position]
                val bundle = Bundle()
                bundle.putInt("id", array[itemposition].food_id)
                bundle.putString("name", array[itemposition].food_name)
                bundle.putString("procedure", array[itemposition].food_procedure)
                bundle.putString("preptime", array[itemposition].food_preptime)
                bundle.putString("ingredients", array[itemposition].food_ingredients)
                bundle.putString("cost", array[itemposition].food_cost)
                bundle.putString("desc", array[itemposition].food_desc)
                bundle.putString("image", array[itemposition].food_image)

                val fragment = RecipeItem()
                fragment.arguments = bundle
                val fragmanager = parentFragmentManager
                val fragtransaction = fragmanager.beginTransaction()
                fragtransaction.replace(R.id.frame_layout, fragment)
                fragtransaction.addToBackStack(null)
                fragtransaction.commit()
            }

        })
    }

    fun filter(search : String){
        filtered_array.clear()
        for (item in reference_array){
            if (item.food_name.toLowerCase().contains(search.toLowerCase()))
                filtered_array.add(item)
        }
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    fun getID(){
        if(searchText.isNullOrBlank()){
            if(category=="All"){
                query ="SELECT * FROM RECIPES"
            } else if(category=="Breakfast"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Almusal%'"
            } else if(category=="Lunch"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Tanghalian%'"
            } else if(category=="Dinner"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Hapunan%'"
            } else if(category=="Snacks"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Meryenda%'"
            } else if(category=="Beverages"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Beverages%'"
            }else if(category=="Healthy"){
            query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Healthy%'"
            }
        }else{
            if(category=="All"){
                query ="SELECT * FROM RECIPES WHERE FoodName LIKE '%$searchText%'"
            } else if(category=="Breakfast"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Almusal%' AND FoodName LIKE '%$searchText%'"
            } else if(category=="Lunch"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Tanghalian%' AND FoodName LIKE '%$searchText%'"
            } else if(category=="Dinner"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Hapunan%' AND FoodName LIKE '%$searchText%'"
            } else if(category=="Snacks"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Meryenda%' AND FoodName LIKE '%$searchText%'"
            } else if(category=="Beverages"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Beverages%' AND FoodName LIKE '%$searchText%'"
            } else if(category=="Healthy"){
                query ="SELECT * FROM RECIPES WHERE FoodDesc LIKE '%Healthy%' AND FoodName LIKE '%$searchText%'"
            }
        }

        var idcursor: Cursor? = db!!.getrecipe_filter(query)
        var filtercursor: Cursor? = db!!.getrecipe_filter(query)

        idList = mutableListOf<Int>()
        while (idcursor!!.moveToNext()){
            val foodid = idcursor.getInt(0)
            idList.add(foodid)
        }
        idcursor.close()
        println(idList)
        filtered_array.clear()
        reference_array.clear()
        while (filtercursor!!.moveToNext()){
            val foodid = filtercursor.getInt(0)
            val foodname = filtercursor.getString(1)
            val foodprocedure = filtercursor.getString(2)
            val preptime = filtercursor.getString(3)
            val foodingredients = filtercursor.getString(4)
            val foodcost = filtercursor.getString(5)
            val fooddesc = filtercursor.getString(6)
            val foodimage = "https://drive.google.com/uc?id=" + filtercursor.getString(7)
            filtered_array.add(RecipeList(foodid, foodname, foodprocedure, preptime, foodingredients, foodcost, fooddesc, foodimage ))
        }
        reference_array.addAll(filtered_array)
        filtercursor.close()
    }
}

