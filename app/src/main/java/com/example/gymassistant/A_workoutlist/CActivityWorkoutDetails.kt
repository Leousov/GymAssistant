package com.example.gymassistant.A_workoutlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymassistant.A_exerciselist.CActivityExerciseDetails
import com.example.gymassistant.A_exerciselist.CRecyclerViewAdapterExercise
import com.example.gymassistant.R
import com.example.gymassistant.databinding.ActivityCworkoutDetailsBinding
import com.example.gymassistant.viewmodel.CViewModelExerciseList
import com.example.gymassistant.viewmodel.CViewModelExerciseListFactory
import com.example.gymassistant.viewmodel.CViewModelWorkoutDetail
import kotlinx.coroutines.launch

// Класс активности для отображения и редактирования деталей тренировки
class CActivityWorkoutDetails : AppCompatActivity() {
    private var id: String? = null // Идентификатор тренировки
    lateinit var resultLauncher: ActivityResultLauncher<Intent> // Лаунчер для получения результата из другой активности
    private lateinit var listAdapter: CRecyclerViewAdapterExercise // Адаптер для RecyclerView
    private val viewModel  : CViewModelWorkoutDetail by viewModels() // Модель прдставления для тренировки
    private lateinit var viewModel_exercise: CViewModelExerciseList  // Модель прдставления для тренировки
    private lateinit var binding: ActivityCworkoutDetailsBinding // Переменная для биндинга
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCworkoutDetailsBinding.inflate(layoutInflater) // Инициализация биндинга
        setContentView(binding.root) // Установка содержимого экрана
        binding.lifecycleOwner = this

        // Получаем данные из Intent (если они были переданы)
        if (!viewModel.initilized.value) {
            intent.extras?.let {bundle ->
                id = bundle.getString(getString(R.string.PARAM_ID))?.let { tempId ->
                    tempId
                }
                id?:run{
                    //Можно сообщение прокрутить через модель представления.
                    Toast.makeText(
                        this@CActivityWorkoutDetails,
                        getString(R.string.INTERNAL_ERROR),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return@let
                }

                //Передаём начальные данные в модель представления.
                viewModel.setItem(
                    id = id!!
                )
            }
        }
        binding.viewModel = viewModel

        // Создаем id, если его не было передано
        id = id ?: getRandomString(32)

        binding.buttonSave.setOnClickListener {
            if (!viewModel.save())
                return@setOnClickListener
            finish() // Закрываем активность
        }

        // Обрабатываем нажатие кнопки "Закрыть"
        binding.buttonClose.setOnClickListener {
            setResult(RESULT_CANCELED) // Устанавливаем результат отмены
            finish() // Закрываем активность
        }

        // Обрабатываем нажатие кнопки "Удалить тренировку"
        binding.buttonDeleteWorkout.setOnClickListener {
            if (!viewModel.delete())
                return@setOnClickListener
            finish() // Закрываем активность
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.message.collect { stringId ->
                    if (stringId < 0)
                        return@collect
                    Toast.makeText(
                        this@CActivityWorkoutDetails,
                        getString(stringId),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }

        id?.let { id ->
            val factory = CViewModelExerciseListFactory(application, id)
            viewModel_exercise = ViewModelProvider(this, factory).get(CViewModelExerciseList::class.java)
        } ?: run {
            Toast.makeText(
                this,
                getString(R.string.INTERNAL_ERROR),
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }

        // Инициализация адаптера для RecyclerView
        listAdapter = CRecyclerViewAdapterExercise(
            clickListener = {exercise ->
                exercise?:return@CRecyclerViewAdapterExercise
                val intent = Intent(
                    this,
                    CActivityExerciseDetails::class.java
                )
                intent.putExtra(getString(R.string.PARAM_ID), exercise.id.toString())
                intent.putExtra(getString(R.string.PARAM2_ID), exercise.workout_id.toString())
                resultLauncher.launch(intent)
            },
            //Обработчик удаления элемента.
            deleteListener = {exercise ->
                viewModel_exercise.deleteItem(exercise)
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
        }
        // Установка слушателя на кнопку для добавления нового упражнения
        binding.button1.setOnClickListener {
            val intent = Intent(this, CActivityExerciseDetails::class.java)
            intent.putExtra(getString(R.string.PARAM_ID), getRandomString(32))
            intent.putExtra(getString(R.string.PARAM2_ID), id)
            resultLauncher.launch(intent)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel_exercise.items.collect { items->
                    //При получении новой версии списка отправляем его в адаптер.
                    //Адаптер сам решает, какие элементы изменились,
                    //и информирует список по нужным элементам.
                    listAdapter.submitList(items)
                }
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
