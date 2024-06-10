package com.example.gymassistant

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymassistant.databinding.ActivityMainBinding
import com.example.gymassistant.viewmodel.CViewModelWorkoutList
import com.example.gymassistant.workoutlist.CActivityWorkoutDetails
import com.example.gymassistant.workoutlist.CRecyclerViewAdapterWorkout
import kotlinx.coroutines.launch

// Основная активность приложения
class MainActivity : AppCompatActivity() {
    private val viewModel: CViewModelWorkoutList by viewModels()
    private lateinit var binding: ActivityMainBinding // Переменная для биндинга
    lateinit var resultLauncher: ActivityResultLauncher<Intent> // Переменная для запуска активностей с результатом
    private var user_id: String = "11" // Идентификатор пользователя
    private lateinit var listAdapter: CRecyclerViewAdapterWorkout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Инициализация биндинга
        setContentView(binding.root) // Установка содержимого экрана

        // Создание начального набора данных для списка тренировок
//        val dataset = mutableListOf<CWorkout>(
//            CWorkout("132", "Ноги", "комплекс упражнений на ноги", user_id),
//            CWorkout("142", "Руки", "комплекс упражнений на руки", user_id),
//            CWorkout("137", "Тело", "комплекс упражнений на тело", user_id)
//        )

        // Создание и настройка адаптера для RecyclerView
        val listAdapter = CRecyclerViewAdapterWorkout(
            clickListener = {workout ->
                workout?:return@CRecyclerViewAdapterWorkout
                val intent = Intent(
                    this,
                    CActivityWorkoutDetails::class.java
                )
                intent.putExtra(getString(R.string.PARAM_ID), workout.id.toString())
                resultLauncher.launch(intent)
            },
            //Обработчик удаления элемента.
            deleteListener = {workout ->
                viewModel.deleteItem(workout)
            }
            )

        // Настройка RecyclerView
        binding.Recycler1.adapter = listAdapter
        val mLayoutManager = LinearLayoutManager(this)
        binding.Recycler1.layoutManager = mLayoutManager

        // Регистрация ActivityResultLauncher для получения результата из активностей
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
//            val bundle = result.data?.extras
//            if (result.resultCode != Activity.RESULT_OK) {
//                // Если результат не ОК, проверяем данные
//                bundle?.let {
//                    val id = bundle.getString("id")
//                    var index = -1
//                    dataset.forEachIndexed { ind, workout ->
//                        if (workout.id == id) {
//                            index = ind
//                        }
//                    }
//                    // Удаление тренировки из списка, если такая существует
//                    if (index >= 0) {
//                        customAdapter.notifyItemRemoved(index)
//                        dataset.removeAt(index)
//                    }
//                }
//                return@registerForActivityResult
//            }
//
//            // Обработка данных из возвращенной активности
//            bundle?.let {
//                val title = bundle.getString("title")
//                val text = bundle.getString("text")
//                val id = bundle.getString("id")
//                var index = -1
//                dataset.forEachIndexed { ind, workout ->
//                    if (workout.id == id) {
//                        workout.text = text
//                        workout.title = title
//                        index = ind
//                    }
//                }
//                // Добавление новой тренировки, если она не была найдена
//                if (index < 0) {
//                    dataset.add(CWorkout(id!!, title, text, user_id))
//                    index = dataset.size - 1
//                }
//                customAdapter.notifyItemChanged(index) // Обновление элемента в списке
//            }
        }

        // Установка слушателя на кнопку для добавления новой тренировки
        binding.button1.setOnClickListener {
            val intent = Intent(this, CActivityWorkoutDetails::class.java)
            resultLauncher.launch(intent)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect { items->
                    //При получении новой версии списка отправляем его в адаптер.
                    //Адаптер сам решает, какие элементы изменились,
                    //и информирует список по нужным элементам.
                    listAdapter.submitList(items)
                }
            }
        }
    }
}
