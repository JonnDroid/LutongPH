package com.example.lutongph.presentation.user_manual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.lutongph.R
import com.example.lutongph.presentation.home.HomeScreen
import kotlinx.android.synthetic.main.activity_user_manual.*
import me.relex.circleindicator.CircleIndicator3

class UserManual : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_manual)

        val btnSkip = findViewById<TextView>(R.id.skip)
        btnSkip.setOnClickListener {
            val Intent = Intent(this, HomeScreen::class.java)
            startActivity(Intent)
        }

        val imagelist = listOf(
            R.drawable.manual_slide1,
            R.drawable.manual_slide2,
            R.drawable.manual_slide3
            //R.drawable.manual_slide4
        )
        val desclist = listOf(
            "Learn how to cook Filipino Cuisines in just one tap!",
            "Create schedules and plans for your meals!",
            "List ingredients available in your home and we'll do the rest!"
            //"Connect with the community and learn more to cook!"
        )
        slider.adapter = ViewPagerAdapter(desclist, imagelist)
        slider.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        
        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(slider)

    }

}