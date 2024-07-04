package com.example.lutongph.data.model

data class RecipeList(var food_id: Int, var food_name: String,var food_procedure: String,var food_preptime: String, var food_ingredients: String, var food_cost: String,var food_desc: String,var food_image: String)
data class IngredientList (var ingredient_id:Int, var food_id:Int, var ingredient_name:String, var ingredient_image:String)
data class SelectedIngredientList (var ingredient_name:String, var ingredient_image:String)
data class FilteredFoodList (var food_name:String, var food_image:String)
data class EventList(var event_food:String, var event_title:String, var event_time:String)
