package com.example.gymassistant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymassistant.databinding.ActivityMainBinding
import com.example.gymassistant.model.CWorkout
import com.example.gymassistant.workoutlist.CActivityWorkoutDetails
import com.example.gymassistant.workoutlist.CRecyclerViewAdapterWorkout

// Основная активность приложения
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // Переменная для биндинга
    lateinit var resultLauncher: ActivityResultLauncher<Intent> // Переменная для запуска активностей с результатом
    private var user_id: String = "11" // Идентификатор пользователя

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Инициализация биндинга
        setContentView(binding.root) // Установка содержимого экрана

        // Создание начального набора данных для списка тренировок
        val dataset = mutableListOf<CWorkout>(
            CWorkout("132", "Ноги", "комплекс упражнений на ноги", user_id),
            CWorkout("142", "Руки", "комплекс упражнений на руки", user_id),
            CWorkout("137", "Тело", "комплекс упражнений на тело", user_id)
        )

        // Создание и настройка адаптера для RecyclerView
        val customAdapter = CRecyclerViewAdapterWorkout(dataset) { workout ->
            // Обработка нажатия на элемент списка тренировок
            val intent = Intent(this, CActivityWorkoutDetails::class.java)
            intent.putExtra("title", workout.title.toString())
            intent.putExtra("text", workout.text.toString())
            intent.putExtra("id", workout.id.toString())
            intent.putExtra("user_id", workout.user_id.toString())
            resultLauncher.launch(intent) // Запуск активности для редактирования тренировки
        }

        // Настройка RecyclerView
        binding.Recycler1.adapter = customAdapter
        val mLayoutManager = LinearLayoutManager(this)
        binding.Recycler1.layoutManager = mLayoutManager

        // Регистрация ActivityResultLauncher для получения результата из активностей
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val bundle = result.data?.extras
            if (result.resultCode != Activity.RESULT_OK) {
                // Если результат не ОК, проверяем данные
                bundle?.let {
                    val id = bundle.getString("id")
                    var index = -1
                    dataset.forEachIndexed { ind, workout ->
                        if (workout.id == id) {
                            index = ind
                        }
                    }
                    // Удаление тренировки из списка, если такая существует
                    if (index >= 0) {
                        customAdapter.notifyItemRemoved(index)
                        dataset.removeAt(index)
                    }
                }
                return@registerForActivityResult
            }

            // Обработка данных из возвращенной активности
            bundle?.let {
                val title = bundle.getString("title")
                val text = bundle.getString("text")
                val id = bundle.getString("id")
                var index = -1
                dataset.forEachIndexed { ind, workout ->
                    if (workout.id == id) {
                        workout.text = text
                        workout.title = title
                        index = ind
                    }
                }
                // Добавление новой тренировки, если она не была найдена
                if (index < 0) {
                    dataset.add(CWorkout(id!!, title, text, user_id))
                    index = dataset.size - 1
                }
                customAdapter.notifyItemChanged(index) // Обновление элемента в списке
            }
        }

        // Установка слушателя на кнопку для добавления новой тренировки
        binding.button1.setOnClickListener {
            val intent = Intent(this, CActivityWorkoutDetails::class.java)
            resultLauncher.launch(intent)
        }
    }
}
