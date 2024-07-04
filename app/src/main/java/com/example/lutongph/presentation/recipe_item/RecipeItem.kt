package com.example.lutongph.presentation.recipe_item

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import java.util.*
import android.content.Context
import android.media.AudioManager
import com.example.lutongph.R
import com.example.lutongph.presentation.recipe.Recipes

class  RecipeItem : Fragment() {
    private lateinit var audio: TextToSpeech
    private lateinit var audioManager: AudioManager
    private var isPlaying: Boolean = false

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_recipe_item, container, false)
        val btnBack = rootView.findViewById<RelativeLayout>(R.id.bar_layout)
        btnBack.setOnClickListener {
            backbutton()
        }

        val fimage: ImageView = rootView.findViewById(R.id.item_image)
        val fname: TextView = rootView.findViewById(R.id.item_name)
        val fprep: TextView = rootView.findViewById(R.id.item_preptime)
        val fprice: TextView = rootView.findViewById(R.id.item_price)
        val ptext: TextView = rootView.findViewById(R.id.procedure_text)
        val itext: TextView = rootView.findViewById(R.id.ingredients_text)
        val play: ImageButton = rootView.findViewById(R.id.procedure_audio)

        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audio = TextToSpeech(requireContext()) { status ->
            if (status != TextToSpeech.ERROR) {
                audio.language = Locale.US

                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0
                )
            }
        }

        play.setImageResource(R.drawable.ic_baseline_play_circle_24)

        play.setOnClickListener {
            val text = ptext.text.toString()
            if (isPlaying) {
                audio.stop()
                play.setImageResource(R.drawable.ic_baseline_play_circle_24)
                isPlaying = false
            } else {
                audio.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                play.setImageResource(R.drawable.ic_baseline_pause_circle_24)
                isPlaying = true
            }
            play.invalidate()
        }

        val bundle: Bundle? = arguments
        val img = bundle!!.getString("image")
        val name = bundle!!.getString("name")
        val prep = bundle!!.getString("preptime")
        val price = bundle!!.getString("cost")
        val procedure = bundle!!.getString("procedure")
        val ing = bundle!!.getString("ingredients")

        val split_steps = procedure!!.split("., ")
        val stepsString = StringBuilder()
        for (sprocedure in split_steps) {
            stepsString.append(sprocedure).append("\n")
        }

        val split_ing = ing!!.split(", ")
        val ingString = StringBuilder()
        for (sing in split_ing) {
            ingString.append(sing).append("\n")
        }

        fname.text = name
        fprep.text = prep
        fprice.text = price
        ptext.text = stepsString.toString()
        itext.text = ingString.toString()
        Picasso.get().load(img).placeholder(R.drawable.ic_baseline_fastfood_24)
            .error(R.drawable.ic_baseline_fastfood_24).into(fimage)

        return rootView

    }

    fun backbutton() {
        val fragment = Recipes()
        val fragmanager = parentFragmentManager
        val fragtransaction = fragmanager.beginTransaction()
        fragtransaction.replace(R.id.frame_layout, fragment)
        fragtransaction.addToBackStack(null)
        fragtransaction.commit()
        onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        audio.stop()
        audio.shutdown()
    }

}