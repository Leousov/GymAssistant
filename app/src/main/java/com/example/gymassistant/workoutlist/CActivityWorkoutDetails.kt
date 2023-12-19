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
import com.example.gymassistant.model.CWorkout

class CActivityWorkoutDetails : AppCompatActivity() { // Детали тренировки
    private var title: String? = null
    private var text: String? = null
    private var id: String? = null
    private var user_id: String? = null
    private lateinit var binding: ActivityCworkoutDetailsBinding
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var listAdapter: CRecyclerViewAdapterExercise
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCworkoutDetailsBinding.inflate(layoutInflater) // Делаем биндинг
        setContentView(binding.root)

        var bundle = intent.extras // Сохраняем пришедшие значения
        bundle?.let{
            title = bundle.getString("title")
            text = bundle.getString("text")
            id = bundle.getString("id")
            user_id = bundle.getString("user_id")
        }
        id = id?: getRandomString(32) // Создаем id если нужно
        binding.textFieldTitle.editText?.setText(title)// выставляем нужные значения на поля
        binding.textFieldText.editText?.setText(text)

        binding.buttonSave.setOnClickListener { // задаем слушатель для кнопки
            val intent = Intent()
            intent.putExtra("title",  binding.textFieldTitle.editText?.text.toString())
            intent.putExtra("text", binding.textFieldText.editText?.text.toString())
            intent.putExtra("id", id.toString())
            intent.putExtra("user_id", user_id.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.buttonClose.setOnClickListener {// на закрытие
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.buttonDeleteWorkout.setOnClickListener {// на закрытие
            val intent = Intent()
            intent.putExtra("id", id.toString())
            setResult(RESULT_CANCELED, intent)
            finish()
        }




        val dataset = mutableListOf<CExercise>( // Это уже идет список упражнений
            CExercise("aaaa", id.toString(), "Приседания" ),
            CExercise("asd", id.toString(), "Отжимания"),
            CExercise("ghjghj", id.toString(), "Планка")
        )
        listAdapter = CRecyclerViewAdapterExercise(
            dataset, // соответствующий адаптер
            clickListener = {exercise ->
                val intent = Intent(
                    this,
                    CActivityExerciseDetails::class.java // Окно отображения упражнения
                )
                intent.putExtra("id", exercise.id.toString())
                intent.putExtra("workout_id", exercise.workout_id.toString())
                intent.putExtra("name", exercise.name.toString())
                intent.putExtra("description", exercise.description.toString())
                intent.putExtra("duration", exercise.duration.toString())
                intent.putExtra("num_sets", exercise.num_sets.toString())
                intent.putExtra("weight", exercise.weight.toString())
                intent.putExtra("times_per_set", exercise.times_per_set.toString())
                resultLauncher.launch(intent)
            },
            deleteListener = {exercise->
                listAdapter.notifyItemRemoved(dataset.indexOf(exercise))
                dataset.remove(exercise)
            }
        )

        binding.Recycler2.adapter = listAdapter
        val mLayoutManager = LinearLayoutManager(this)
        binding.Recycler2.layoutManager =  mLayoutManager

        resultLauncher = registerForActivityResult( //обработчик событий по кнопкам
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if (result.resultCode != Activity.RESULT_OK)
                return@registerForActivityResult

            val bundle = result.data?.extras
            bundle?.let {
                val id = bundle.getString("id")
                val workout_id = bundle.getString("workout_id")
                val name = bundle.getString("name")
                val description = bundle.getString("description")
                val duration = bundle.getString("duration")
                val num_sets = bundle.getString("num_sets")
                val weight = bundle.getString("weight")
                val times_per_set = bundle.getString("times_per_set")
                var index = -1
                dataset.forEachIndexed{ind, exercise ->
                    if (exercise.id == id){
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
                if (index < 0){
                    dataset.add(CExercise(
                        id.toString(),
                        workout_id.toString(),
                        name.toString(),
                        description.toString(),
                        duration?.toInt() ?: -1,
                        num_sets?.toInt() ?: -1,
                        weight?.toDouble() ?: -1.0,
                        times_per_set?.toInt() ?: -1))
                    index = dataset.size -1
                }
                listAdapter.notifyItemChanged(index)
            }
        }
    }
}
fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}