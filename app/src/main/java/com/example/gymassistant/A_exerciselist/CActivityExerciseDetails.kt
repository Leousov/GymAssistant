package com.example.gymassistant.A_exerciselist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gymassistant.A_workoutlist.getRandomString
import com.example.gymassistant.ChartActivity
import com.example.gymassistant.R
import com.example.gymassistant.databinding.ActivityCexerciseDetailsBinding
import com.example.gymassistant.viewmodel.exercise.CViewModelExerciseDetail
import kotlinx.coroutines.launch

// Основной класс активности для отображения и редактирования деталей упражнения
class CActivityExerciseDetails : AppCompatActivity() {
    private var id: String? = null // Идентификатор упражнения
    private var workout_id: String? = null // Идентификатор тренировки
    private lateinit var binding: ActivityCexerciseDetailsBinding // Переменная для биндинга
    private val viewModel  : CViewModelExerciseDetail by viewModels() // Модель представления для упражнения
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCexerciseDetailsBinding.inflate(layoutInflater) // Инициализация биндинга
        setContentView(binding.root) // Установка содержимого экрана
        binding.lifecycleOwner = this


        if (!viewModel.initilized.value) {
            intent.extras?.let {bundle ->
                id = bundle.getString(getString(R.string.PARAM_ID))?.let { tempId ->
                    tempId
                }
                workout_id = bundle.getString(getString(R.string.PARAM2_ID))?.let { tempId ->
                    tempId
                }

                //Если идентификатор не указан,
                //выдаём сообщение
                id?:run{
                    //Можно сообщение прокрутить через модель представления.
                    Toast.makeText(
                        this@CActivityExerciseDetails,
                        getString(R.string.INTERNAL_ERROR),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return@let
                }

                //Передаём начальные данные в модель представления.
                viewModel.setItem(
                    id = id!!,
                    workout_id = workout_id!!
                )
            }
        }
        binding.viewModel = viewModel
        // Создаем id, если его не было передано
        id = id ?: getRandomString(32)


        // Обрабатываем нажатие кнопки "Сохранить"
        binding.buttonSave.setOnClickListener {
            if (!viewModel.save())
                return@setOnClickListener
            finish() // Закрываем активность
        }

        // Обрабатываем нажатие кнопки "Отмена"
        binding.buttonCancel.setOnClickListener {
            setResult(RESULT_CANCELED) // Устанавливаем результат отмены
            finish() // Закрываем активность
        }
        binding.buttonDelete.setOnClickListener {
            if (!viewModel.delete())
                return@setOnClickListener
            finish() // Закрываем активность
        }
        binding.buttonGraf.setOnClickListener {
            val intent = Intent(this, ChartActivity::class.java)
            intent.putExtra(getString(R.string.PARAM_ID), id)
            resultLauncher.launch(intent)
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.message.collect { stringId ->
                    if (stringId < 0)
                        return@collect
                    //При получении информации о сообщении,
                    //выводим его на экран.
                    Toast.makeText(
                        this@CActivityExerciseDetails,
                        getString(stringId),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }
}
