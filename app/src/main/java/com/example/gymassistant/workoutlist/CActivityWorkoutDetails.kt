package com.example.gymassistant.workoutlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymassistant.databinding.ActivityCworkoutDetailsBinding
import com.example.gymassistant.exerciselist.CActivityExerciseDetails
import com.example.gymassistant.exerciselist.CRecyclerViewAdapterExercise
import com.example.gymassistant.model.CExercise

// Класс активности для отображения и редактирования деталей тренировки
class CActivityWorkoutDetails : AppCompatActivity() {
    private var title: String? = null // Название тренировки
    private var text: String? = null // Описание тренировки
    private var id: String? = null // Идентификатор тренировки
    private var user_id: String? = null // Идентификатор пользователя
    private lateinit var binding: ActivityCworkoutDetailsBinding // Переменная для биндинга
    lateinit var resultLauncher: ActivityResultLauncher<Intent> // Лаунчер для получения результата из другой активности
    private lateinit var listAdapter: CRecyclerViewAdapterExercise // Адаптер для RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCworkoutDetailsBinding.inflate(layoutInflater) // Инициализация биндинга
        setContentView(binding.root) // Установка содержимого экрана

        // Получаем данные из Intent (если они были переданы)
        val bundle = intent.extras
        bundle?.let {
            title = it.getString("title")
            text = it.getString("text")
            id = it.getString("id")
            user_id = it.getString("user_id")
        }

        // Создаем id, если его не было передано
        id = id ?: getRandomString(32)

        // Заполняем поля данными
        binding.textFieldTitle.editText?.setText(title)
        binding.textFieldText.editText?.setText(text)

        // Обрабатываем нажатие кнопки "Сохранить"
        binding.buttonSave.setOnClickListener {
            val intent = Intent()
            intent.putExtra("title", binding.textFieldTitle.editText?.text.toString())
            intent.putExtra("text", binding.textFieldText.editText?.text.toString())
            intent.putExtra("id", id.toString())
            intent.putExtra("user_id", user_id.toString())
            setResult(RESULT_OK, intent) // Устанавливаем результат и возвращаем данные
            finish() // Закрываем активность
        }

        // Обрабатываем нажатие кнопки "Закрыть"
        binding.buttonClose.setOnClickListener {
            setResult(RESULT_CANCELED) // Устанавливаем результат отмены
            finish() // Закрываем активность
        }

        // Обрабатываем нажатие кнопки "Удалить тренировку"
        binding.buttonDeleteWorkout.setOnClickListener {
            val intent = Intent()
            intent.putExtra("id", id.toString())
            setResult(RESULT_CANCELED, intent)
            finish() // Закрываем активность
        }

        // Создаем список упражнений (для примера)
        val dataset = mutableListOf<CExercise>(
            CExercise("aaaa", id.toString(), "Приседания"),
            CExercise("asd", id.toString(), "Отжимания"),
            CExercise("ghjghj", id.toString(), "Планка")
        )

        // Инициализация адаптера для RecyclerView
        listAdapter = CRecyclerViewAdapterExercise(
            dataset,
            clickListener = { exercise ->
                val intent = Intent(this, CActivityExerciseDetails::class.java)
                intent.putExtra("id", exercise.id.toString())
                intent.putExtra("workout_id", exercise.workout_id.toString())
                intent.putExtra("name", exercise.name.toString())
                intent.putExtra("description", exercise.description.toString())
                intent.putExtra("duration", exercise.duration.toString())
                intent.putExtra("num_sets", exercise.num_sets.toString())
                intent.putExtra("weight", exercise.weight.toString())
                intent.putExtra("times_per_set", exercise.times_per_set.toString())
                resultLauncher.launch(intent) // Запускаем активность для результата
            },
            deleteListener = { exercise ->
                listAdapter.notifyItemRemoved(dataset.indexOf(exercise)) // Уведомляем адаптер об удалении элемента
                dataset.remove(exercise) // Удаляем элемент из списка
            }
        )

        // Настройка RecyclerView
        binding.Recycler2.adapter = listAdapter
        val mLayoutManager = LinearLayoutManager(this)
        binding.Recycler2.layoutManager = mLayoutManager

        // Регистрация обработчика результата активности
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult

            val bundle = result.data?.extras
            bundle?.let {
                val id = it.getString("id")
                val workout_id = it.getString("workout_id")
                val name = it.getString("name")
                val description = it.getString("description")
                val duration = it.getString("duration")
                val num_sets = it.getString("num_sets")
                val weight = it.getString("weight")
                val times_per_set = it.getString("times_per_set")
                var index = -1
                dataset.forEachIndexed { ind, exercise ->
                    if (exercise.id == id) {
                        exercise.workout_id = workout_id.toString()
                        exercise.name = name.toString()
                        exercise.description = description.toString()
                        exercise.duration = duration?.toInt() ?: -1
                        exercise.num_sets = num_sets?.toInt() ?: -1
                        exercise.weight = weight?.toDouble() ?: -1.0
                        exercise.times_per_set = times_per_set?.toInt() ?: -1
                        index = ind
                    }
                }
                if (index < 0) {
                    dataset.add(CExercise(
                        id.toString(),
                        workout_id.toString(),
                        name.toString(),
                        description.toString(),
                        duration?.toInt() ?: -1,
                        num_sets?.toInt() ?: -1,
                        weight?.toDouble() ?: -1.0,
                        times_per_set?.toInt() ?: -1
                    ))
                    index = dataset.size - 1
                }
                listAdapter.notifyItemChanged(index) // Уведомляем адаптер об изменении элемента
            }
        }
    }
}

// Функция для генерации случайной строки
fun getRandomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}
