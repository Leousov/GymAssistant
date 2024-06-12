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
import com.example.gymassistant.A_sessionlist.CActivitySessionDetails
import com.example.gymassistant.A_workoutlist.CActivityWorkoutDetails
import com.example.gymassistant.A_workoutlist.CRecyclerViewAdapterWorkout
import com.example.gymassistant.A_workoutlist.getRandomString
import com.example.gymassistant.databinding.ActivityMainBinding
import com.example.gymassistant.viewmodel.CViewModelWorkoutList
import kotlinx.coroutines.launch

// Основная активность приложения
class MainActivity : AppCompatActivity() {
    private val viewModel: CViewModelWorkoutList by viewModels()
    private lateinit var binding: ActivityMainBinding // Переменная для биндинга
    lateinit var resultLauncher: ActivityResultLauncher<Intent> // Переменная для запуска активностей с результатом

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Инициализация биндинга
        setContentView(binding.root) // Установка содержимого экрана

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
            sessionListener = {id ->
                val intent = Intent(this, CActivitySessionDetails::class.java)
                intent.putExtra(getString(R.string.PARAM_ID), getRandomString(32))
                intent.putExtra(getString(R.string.PARAM2_ID), id)
                resultLauncher.launch(intent)
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
        }

        // Установка слушателя на кнопку для добавления новой тренировки
        binding.button1.setOnClickListener {
            val intent = Intent(this, CActivityWorkoutDetails::class.java)
            intent.putExtra(getString(R.string.PARAM_ID), getRandomString(32))
            resultLauncher.launch(intent)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect { items->
                    listAdapter.submitList(items)
                }
            }
        }
    }
}
