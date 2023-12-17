package com.example.gymassistant.workoutlist

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymassistant.CActivityWorkoutDetails
import com.example.gymassistant.R
import com.example.gymassistant.databinding.ActivityMainBinding
import com.example.gymassistant.model.CWorkout


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dataset = arrayOf(CWorkout("aaaa","asdasdasd"), CWorkout("asd","sdg"), CWorkout("ghjghj","qweqw"))
        val customAdapter = CRecyclerViewAdapterWorkout(dataset)

        binding.Recycler1.adapter = customAdapter
        val mLayoutManager = LinearLayoutManager(this)
        binding.Recycler1.layoutManager =  mLayoutManager

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data?.extras
                val x = 333
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
