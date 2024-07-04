package com.example.lutongph.presentation.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lutongph.R
import com.example.lutongph.presentation.recipe.Recipes
import com.example.lutongph.databinding.ActivityHomescreenBinding
import com.example.lutongph.presentation.MainActivity
import com.example.lutongph.presentation.calendar.Calendar
import com.example.lutongph.presentation.ingredient.Ingredients

class HomeScreen : AppCompatActivity() {

    private lateinit var binding: ActivityHomescreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomescreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Recipes())

        binding.navbar.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.recipes -> replaceFragment(Recipes())
                R.id.ingredients -> replaceFragment(Ingredients())
                R.id.calendar -> replaceFragment(Calendar())
                R.id.logout -> logout()
                else -> {}

            }
            true
        }
    }

    private fun logout() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.logout_prompt)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val bYes: Button = dialog.findViewById(R.id.btnYes)
        val bNo: Button = dialog.findViewById(R.id.btnNo)


        bYes.setOnClickListener {
            val Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
        }
        bNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmanager = supportFragmentManager
        val fragtransaction = fragmanager.beginTransaction()
        fragtransaction.replace(R.id.frame_layout, fragment)
        fragtransaction.addToBackStack(null)
        fragtransaction.commit()
    }
}