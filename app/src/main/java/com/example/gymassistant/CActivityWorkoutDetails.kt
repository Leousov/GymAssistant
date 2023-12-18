package com.example.gymassistant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gymassistant.databinding.ActivityCworkoutDetailsBinding

class CActivityWorkoutDetails : AppCompatActivity() {
    private var title: String? = null
    private var text: String? = null
    private var id: String? = null
    private var user_id: String? = null


    private lateinit var binding: ActivityCworkoutDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCworkoutDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var bundle = intent.extras
        bundle?.let{
            title = bundle.getString("title")
            text = bundle.getString("text")
            id = bundle.getString("id")
            user_id = bundle.getString("user_id")
        }
        id = id?: getRandomString(32)
        binding.textFieldTitle.editText?.setText(title)
        binding.textFieldText.editText?.setText(text)

        binding.buttonSave.setOnClickListener {
            val intent = Intent()
            intent.putExtra("title",  binding.textFieldTitle.editText?.text)
            intent.putExtra("text", binding.textFieldText.editText?.text)
            intent.putExtra("id", id)
            intent.putExtra("user_id", user_id)
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.buttonClose.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
//        У меня нет ID
    }
}
fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}