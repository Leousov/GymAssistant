package com.example.gymassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gymassistant.databinding.ActivityCworkoutDetailsBinding
import com.example.gymassistant.databinding.ActivityMainBinding
import com.example.gymassistant.model.CWorkout

class CActivityWorkoutDetails : AppCompatActivity() {
    private var title: String? = null
    private var text: String? = null

    private lateinit var binding: ActivityCworkoutDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCworkoutDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var bundle = intent.extras
        bundle?.let{
            title = bundle.getString("title")
            text = bundle.getString("text")
        }
        binding.textFieldTitle.editText?.setText(title)
        binding.textFieldText.editText?.setText(text)

        binding.buttonSave.setOnClickListener {
              val intent = Intent()
            intent.putExtra("param2", "sdfsdfsfsd")
            setResult(RESULT_OK, intent)
            finish()
        }
//        У меня нет ID
    }
}