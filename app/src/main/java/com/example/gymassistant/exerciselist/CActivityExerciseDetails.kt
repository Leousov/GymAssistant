package com.example.gymassistant.exerciselist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gymassistant.R
import com.example.gymassistant.databinding.ActivityCexerciseDetailsBinding
import com.example.gymassistant.workoutlist.getRandomString

class CActivityExerciseDetails : AppCompatActivity() {
    private var id: String?= null
    private var workout_id: String?= null
    private var name: String? = null
    private var description: String? = ""
    private var duration: Int? = -1
    private var num_sets: Int? = -1
    private var weight: Double? = -1.0
    private var times_per_set: Int? = -1
    private lateinit var binding: ActivityCexerciseDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCexerciseDetailsBinding.inflate(layoutInflater) // Делаем биндинг
        setContentView(binding.root)

        var bundle = intent.extras // Сохраняем пришедшие значения
        bundle?.let{
            id = bundle.getString("id").toString()
            workout_id = bundle.getString("workout_id").toString()
            name = bundle.getString("name").toString()
            description = bundle.getString("description").toString()
            duration = bundle.getString("duration")?.toInt() ?: -1
            num_sets = bundle.getString("num_sets")?.toInt() ?: -1
            weight = bundle.getString("weight")?.toDouble() ?: -1.0
            times_per_set = bundle.getString("times_per_set")?.toInt() ?: -1
        }
        id = id?: getRandomString(32) // Создаем id если нужно
        // выставляем нужные значения на поля
        binding.textViewname.editText?.setText(name)
        binding.textViewdescription.editText?.setText(description)
        binding.textViewduration.editText?.setText(duration.toString())
        binding.textViewnumSets.editText?.setText(num_sets.toString())
        binding.textViewweight.editText?.setText(weight.toString())
        binding.textViewTimesPerSet.editText?.setText(times_per_set.toString())

        binding.buttonSave.setOnClickListener { // задаем слушатель для кнопки
            val intent = Intent()
            intent.putExtra("id", id.toString())
            intent.putExtra("workout_id", workout_id.toString())
            intent.putExtra("name", binding.textViewname.editText?.text.toString())
            intent.putExtra("description", binding.textViewdescription.editText?.text.toString())
            intent.putExtra("duration", binding.textViewduration.editText?.text.toString())
            intent.putExtra("num_sets", binding.textViewnumSets.editText?.text.toString())
            intent.putExtra("weight", binding.textViewweight.editText?.text.toString())
            intent.putExtra("times_per_set", binding.textViewTimesPerSet.editText?.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.buttonCancel.setOnClickListener {// на закрытие
            setResult(RESULT_CANCELED)
            finish()
        }


    }
}