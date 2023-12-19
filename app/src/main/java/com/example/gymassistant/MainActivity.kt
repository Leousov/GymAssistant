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


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var user_id:String = "11"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dataset = mutableListOf<CWorkout>(
            CWorkout("132", "Ноги", "комплекс упражнений на ноги", user_id),
            CWorkout("142", "Руки", "комплекс упражнений на руки", user_id),
            CWorkout("137", "Тело", "комплекс упражнений на тело", user_id))
        val customAdapter = CRecyclerViewAdapterWorkout(
            dataset
        ){workout ->
            val intent = Intent(
                this,
                CActivityWorkoutDetails::class.java
            )
            intent.putExtra("title",  workout.title.toString())
            intent.putExtra("text", workout.text.toString())
            intent.putExtra("id", workout.id.toString())
            intent.putExtra("user_id", workout.user_id.toString())

            resultLauncher.launch(intent)

        }

        binding.Recycler1.adapter = customAdapter
        val mLayoutManager = LinearLayoutManager(this)
        binding.Recycler1.layoutManager =  mLayoutManager

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            val bundle = result.data?.extras
            if (result.resultCode != Activity.RESULT_OK)
            {
                bundle?.let {
                    val id = bundle.getString("id")
                    var index = -1
                    dataset.forEachIndexed{ind, workout ->
                        if (workout.id == id){
                            index = ind
                        }
                    }

                    if (index >= 0){
                        customAdapter.notifyItemRemoved(index)
                        dataset.removeAt(index)
                    }

                }
                return@registerForActivityResult
            }




            bundle?.let {
                val title = bundle.getString("title")
                val text = bundle.getString("text")
                val id = bundle.getString("id")
                var index = -1
                dataset.forEachIndexed{ind, workout ->
                    if (workout.id == id){
                        workout.text = text
                        workout.title = title
                        index = ind
                    }
                }
                if (index < 0){
                    dataset.add(CWorkout(id!!, title, text, user_id))
                    index = dataset.size -1
                }
                customAdapter.notifyItemChanged(index)
            }
        }
        binding.button1.setOnClickListener{
            val intent = Intent(
                this,
                CActivityWorkoutDetails::class.java
            )
//                .apply {
//                putExtra("param1", "123")
//            }
            resultLauncher.launch(intent)
        }
    }
}
