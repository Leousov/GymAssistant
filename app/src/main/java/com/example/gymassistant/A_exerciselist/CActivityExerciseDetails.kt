package com.example.gymassistant.A_exerciselist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gymassistant.A_workoutlist.getRandomString
import com.example.gymassistant.R
import com.example.gymassistant.databinding.ActivityCexerciseDetailsBinding
import com.example.gymassistant.viewmodel.CViewModelExerciseDetail
import kotlinx.coroutines.launch

// Основной класс активности для отображения и редактирования деталей упражнения
class CActivityExerciseDetails : AppCompatActivity() {
    private var id: String? = null // Идентификатор упражнения
    private var workout_id: String? = null // Идентификатор тренировки
    private var name: String? = null // Название упражнения
    private var description: String? = "" // Описание упражнения
    private var duration: Int? = null // Продолжительность упражнения
    private var num_sets: Int? = null // Количество подходов
    private var weight: Double? = null // Вес
    private var times_per_set: Int? = null // Количество повторений в подходе
    private lateinit var binding: ActivityCexerciseDetailsBinding // Переменная для биндинга
    lateinit var resultLauncher: ActivityResultLauncher<Intent> // Лаунчер для получения результата из другой активности
    private val viewModel  : CViewModelExerciseDetail by viewModels() // Модель представления для упражнения

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCexerciseDetailsBinding.inflate(layoutInflater) // Инициализация биндинга
        setContentView(binding.root) // Установка содержимого экрана
        binding.lifecycleOwner = this

        // Получаем данные из Intent (если они были переданы)
//        val bundle = intent.extras
//        bundle?.let {
//            id = bundle.getString("id").toString()
//            workout_id = bundle.getString("workout_id").toString()
//            name = bundle.getString("name").toString()
//            description = bundle.getString("description").toString()
//            duration = bundle.getString("duration")?.toInt()
//            num_sets = bundle.getString("num_sets")?.toInt()
//            weight = bundle.getString("weight")?.toDouble()
//            times_per_set = bundle.getString("times_per_set")?.toInt()
//        }

        if (!viewModel.initilized.value) {
            intent.extras?.let {bundle ->
                id = bundle.getString(getString(R.string.PARAM_ID))?.let { tempId ->
                    tempId
                }
                val workout_id = bundle.getString(getString(R.string.PARAM2_ID))?.let { tempId ->
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

        // Заполняем поля данными
//        binding.textViewname.editText?.setText(name)
//        binding.textViewdescription.editText?.setText(description)
//        binding.textViewduration.editText?.setText( if (duration!! > 0 ) duration.toString() else "" )
//        binding.textViewnumSets.editText?.setText( if (num_sets!! > 0 ) num_sets.toString() else "")
//        binding.textViewweight.editText?.setText( if (weight!! > 0 ) weight.toString() else "")
//        binding.textViewTimesPerSet.editText?.setText( if (times_per_set!! > 0 ) times_per_set.toString() else "")

        // Обрабатываем нажатие кнопки "Сохранить"
        binding.buttonSave.setOnClickListener {
            if (!viewModel.save())
                return@setOnClickListener
//            val intent = Intent()
//            intent.putExtra("id", id.toString())
//            intent.putExtra("workout_id", workout_id.toString())
//            intent.putExtra("name", binding.textViewname.editText?.text.toString())
//            intent.putExtra("description", if (binding.textViewdescription.editText?.text.toString() == "") "" else binding.textViewdescription.editText?.text.toString())
//            intent.putExtra("duration", if (binding.textViewduration.editText?.text.toString() == "") "-1" else binding.textViewduration.editText?.text.toString())
//            intent.putExtra("num_sets", if (binding.textViewnumSets.editText?.text.toString() == "") "-1" else binding.textViewnumSets.editText?.text.toString())
//            intent.putExtra("weight", if (binding.textViewweight.editText?.text.toString() == "") "-1" else binding.textViewweight.editText?.text.toString())
//            intent.putExtra("times_per_set", if (binding.textViewTimesPerSet.editText?.text.toString() == "") "-1" else binding.textViewTimesPerSet.editText?.text.toString())
//
//            Log.d("MyApp", binding.textViewname.editText?.text.toString())
//            Log.d("MyApp", (binding.textViewdescription.editText?.text.toString() == "").toString())
//            Log.d("MyApp", if (binding.textViewdescription.editText?.text.toString() == "") "" else binding.textViewdescription.editText?.text.toString())
//            Log.d("MyApp", binding.textViewduration.editText?.text.toString())
//            Log.d("MyApp", binding.textViewnumSets.editText?.text.toString())
//            Log.d("MyApp", binding.textViewname.editText?.text.toString())
//            setResult(RESULT_OK, intent) // Устанавливаем результат и возвращаем данные
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
