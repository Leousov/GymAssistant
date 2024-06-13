package com.example.gymassistant.A_sessionlist

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
import com.example.gymassistant.A_workoutlist.getRandomString
import com.example.gymassistant.R
import com.example.gymassistant.databinding.ActivityCsessionDetailsBinding
import com.example.gymassistant.viewmodel.session.CViewModelSessionDetail
import com.example.gymassistant.viewmodel.session.CViewModelSessionExerciseList
import com.example.gymassistant.viewmodel.session.CViewModelSessionExerciseListFactory
import kotlinx.coroutines.launch

class CActivitySessionDetails : AppCompatActivity()  {
    private var id: String? = null // Идентификатор сессии
    private var workout_id: String? = null // Идентификатор тренировки
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val viewModel  : CViewModelSessionDetail by viewModels()
    private lateinit var viewModel_exercise  : CViewModelSessionExerciseList
    private lateinit var binding: ActivityCsessionDetailsBinding
    private lateinit var listAdapter: CRecyclerViewAdapterSessionExercise
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCsessionDetailsBinding.inflate(layoutInflater) // Инициализация биндинга
        setContentView(binding.root) // Установка содержимого экрана
        binding.lifecycleOwner = this

        // Получаем данные из Intent (если они были переданы)
        if (!viewModel.initilized.value) {
            intent.extras?.let {bundle ->
                id = bundle.getString(getString(R.string.PARAM_ID))?.let { tempId ->
                    tempId
                }
                //Если идентификатор не указан,
                //выдаём сообщение
                id?:run{
                    //Можно сообщение прокрутить через модель представления.
                    Toast.makeText(
                        this@CActivitySessionDetails,
                        getString(R.string.INTERNAL_ERROR),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return@let
                }
                workout_id =  bundle.getString(getString(R.string.PARAM2_ID))?.let { tempId ->
                    tempId
                }
                //Передаём начальные данные в модель представления.
                viewModel.setItem(
                    id = id!! ,
                    workout_id = workout_id!!
                )
            }
        }
        binding.viewModel = viewModel
//        binding.workout =

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.message.collect { stringId ->
                    if (stringId < 0)
                        return@collect
                    //При получении информации о сообщении,
                    //выводим его на экран.
                    Toast.makeText(
                        this@CActivitySessionDetails,
                        getString(stringId),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
        }

        id?.let { id ->
            workout_id?.let{workout_id ->
                val factory = CViewModelSessionExerciseListFactory(application, workout_id, id)
                viewModel_exercise = ViewModelProvider(this, factory).get(
                    CViewModelSessionExerciseList::class.java)
            }

        } ?: run {
            // Если workoutId равен null, покажите сообщение об ошибке и завершите Activity
            Toast.makeText(
                this,
                getString(R.string.INTERNAL_ERROR),
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }

        // Инициализация адаптера для RecyclerView
        listAdapter = CRecyclerViewAdapterSessionExercise(
            viewModel = viewModel_exercise,
            ListenerUp = {session_exercise ->
                if (session_exercise.sets_done!! < session_exercise.num_sets!!){
                    session_exercise.sets_done = session_exercise.sets_done!! + 1
                    viewModel_exercise.update(session_exercise)
                    listAdapter.notifyDataSetChanged()
                }
            },
            ListenerDown = {session_exercise ->
                if (session_exercise.sets_done!! > 0) {
                    session_exercise.sets_done = session_exercise.sets_done!! - 1
                    viewModel_exercise.update(session_exercise)
                    listAdapter.notifyDataSetChanged()
                }
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