package com.example.gymassistant.exerciselist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gymassistant.databinding.ActivityCexerciseDetailsBinding
import com.example.gymassistant.workoutlist.getRandomString

// Основной класс активности для отображения и редактирования деталей упражнения
class CActivityExerciseDetails : AppCompatActivity() {
    private var id: String? = null // Идентификатор упражнения
    private var workout_id: String? = null // Идентификатор тренировки
    private var name: String? = null // Название упражнения
    private var description: String? = "" // Описание упражнения
    private var duration: Int? = -1 // Продолжительность упражнения
    private var num_sets: Int? = -1 // Количество подходов
    private var weight: Double? = -1.0 // Вес
    private var times_per_set: Int? = -1 // Количество повторений в подходе
    private lateinit var binding: ActivityCexerciseDetailsBinding // Переменная для биндинга

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCexerciseDetailsBinding.inflate(layoutInflater) // Инициализация биндинга
        setContentView(binding.root) // Установка содержимого экрана

        // Получаем данные из Intent (если они были переданы)
        val bundle = intent.extras
        bundle?.let {
            id = bundle.getString("id").toString()
            workout_id = bundle.getString("workout_id").toString()
            name = bundle.getString("name").toString()
            description = bundle.getString("description").toString()
            duration = bundle.getString("duration")?.toInt() ?: -1
            num_sets = bundle.getString("num_sets")?.toInt() ?: -1
            weight = bundle.getString("weight")?.toDouble() ?: -1.0
            times_per_set = bundle.getString("times_per_set")?.toInt() ?: -1
        }

        // Создаем id, если его не было передано
        id = id ?: getRandomString(32)

        // Заполняем поля данными
        binding.textViewname.editText?.setText(name)
        binding.textViewdescription.editText?.setText(description)
        binding.textViewduration.editText?.setText(duration.toString())
        binding.textViewnumSets.editText?.setText(num_sets.toString())
        binding.textViewweight.editText?.setText(weight.toString())
        binding.textViewTimesPerSet.editText?.setText(times_per_set.toString())

        // Обрабатываем нажатие кнопки "Сохранить"
        binding.buttonSave.setOnClickListener {
            val intent = Intent()
            intent.putExtra("id", id.toString())
            intent.putExtra("workout_id", workout_id.toString())
            intent.putExtra("name", binding.textViewname.editText?.text.toString())
            intent.putExtra("description", binding.textViewdescription.editText?.text.toString())
            intent.putExtra("duration", binding.textViewduration.editText?.text.toString())
            intent.putExtra("num_sets", binding.textViewnumSets.editText?.text.toString())
            intent.putExtra("weight", binding.textViewweight.editText?.text.toString())
            intent.putExtra("times_per_set", binding.textViewTimesPerSet.editText?.text.toString())
            setResult(RESULT_OK, intent) // Устанавливаем результат и возвращаем данные
            finish() // Закрываем активность
        }

        // Обрабатываем нажатие кнопки "Отмена"
        binding.buttonCancel.setOnClickListener {
            setResult(RESULT_CANCELED) // Устанавливаем результат отмены
            finish() // Закрываем активность
        }
    }
}
