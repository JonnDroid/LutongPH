package com.example.lutongph.presentation.ingredient

import android.database.Cursor
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lutongph.R
import com.example.lutongph.data.model.FilteredFoodList
import com.example.lutongph.data.model.IngredientList
import com.example.lutongph.data.model.SelectedIngredientList
import com.example.lutongph.util.DBHelper
import kotlin.collections.ArrayList

class Ingredients : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gridView: GridView
    private lateinit var adapter: IngredientRecyclerAdapter
    private lateinit var grid_adapter: IngredientGridAdapter
    private lateinit var db: DBHelper
    private lateinit var i_array: ArrayList<IngredientList>
    private lateinit var si_array: ArrayList<SelectedIngredientList>
    private lateinit var temp_array: ArrayList<SelectedIngredientList>
    private lateinit var sr_array: ArrayList<FilteredFoodList>
    private lateinit var siList: ArrayList<String>
    var query: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_ingredients, container, false)

        si_array = ArrayList()
        sr_array = ArrayList()
        i_array = ArrayList()


        recyclerView = view.findViewById(R.id.ingredients_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.setHasFixedSize(true)

        gridView = view.findViewById(R.id.food_gridview)

        db = DBHelper(view.context)
        initsearchcontent()

        val ingredients = i_array.map { it.ingredient_name }
        val autoCompleteAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ingredients)

        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.search_view)
        autoCompleteTextView.setAdapter(autoCompleteAdapter)
        autoCompleteTextView.threshold = 1
        var counter: Int = 0
        siList = ArrayList<String>()
        autoCompleteTextView.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                true
            } else {
                val selectedIngredient = textView.text.toString()

                var siCursor: Cursor? = db.getingredients_specific(selectedIngredient)
                temp_array = ArrayList<SelectedIngredientList>()
                while (siCursor!!.moveToNext()) {
                    val ingName = siCursor.getString(0)
                    val ingImage = "https://drive.google.com/uc?id=" + siCursor.getString(1)
                    val temp_list = SelectedIngredientList(ingName, ingImage)
                    if (!temp_array.contains(temp_list)) {
                        temp_array.add(temp_list)
                        siList += selectedIngredient
                        counter++
                    }
                    if (temp_array.size >= 2) {
                        break
                    }

                }

                siCursor.close()
                temp_array.removeAll(si_array)
                si_array.addAll(temp_array)
                adapter = IngredientRecyclerAdapter(si_array)
                recyclerView.adapter = adapter

                updatequery()

                adapter.setremoveOnItemClickListener(object :
                    IngredientRecyclerAdapter.removeOnItemClickListener {
                    override fun onItemClick(position: Int) {
                        si_array.removeAt(position)
                        adapter.notifyItemRemoved(position)

                        siList.removeAt(position)
                        updatequery()

                        var srCursor: Cursor? = db.getrecipe_specific(query)
                        sr_array.clear()
                        while (srCursor!!.moveToNext()) {
                            val foodName = srCursor.getString(0)
                            val foodImage =
                                "https://drive.google.com/uc?id=" + srCursor.getString(1)
                            sr_array.add(FilteredFoodList(foodName, foodImage))
                        }
                        srCursor.close()
                        grid_adapter = IngredientGridAdapter(requireContext(), sr_array)
                        gridView.adapter = grid_adapter

                        counter--
                        if (counter == 0) {
                            gridView.adapter = null
                        }
                        checker()
                    }
                })

                var srCursor: Cursor? = db.getrecipe_specific(query)
                sr_array.clear()
                while (srCursor!!.moveToNext()) {
                    val foodName = srCursor.getString(0)
                    val foodImage = "https://drive.google.com/uc?id=" + srCursor.getString(1)
                    sr_array.add(FilteredFoodList(foodName, foodImage))
                }
                srCursor.close()
                grid_adapter = IngredientGridAdapter(requireContext(), sr_array)
                gridView.adapter = grid_adapter

                autoCompleteTextView.setText("")

                checker()

                false
            }
        }

        return view
    }

    fun updatequery() {
        for (i in siList.indices) {
            if (i == 0) {
                query =
                    "SELECT FoodName, Image FROM INGREDIENTS i INNER JOIN RECIPES r ON i.FoodID = r.FoodID WHERE IngredientName LIKE '${siList[i]}'"
            } else {
                query += " AND EXISTS (SELECT 1 FROM INGREDIENTS i2 WHERE i2.FoodID = r.FoodID AND i2.IngredientName LIKE '${siList[i]}')"
            }
        }
    }

    fun checker() {
        val items = arrayListOf<String>(query)

        for (i in 0 until items.size) {
            println(items[i])
        }
    }

    private fun initsearchcontent() {

        var newcursor: Cursor? = db!!.getingredients()
        while (newcursor!!.moveToNext()) {
            val ing_id = newcursor.getInt(1)
            val food_id = newcursor.getInt(2)
            val ing_name = newcursor.getString(3)
            val ing_image = "https://drive.google.com/uc?id=" + newcursor.getString(4)
            i_array.add(IngredientList(ing_id, food_id, ing_name, ing_image))
        }
        newcursor.close()
    }
}