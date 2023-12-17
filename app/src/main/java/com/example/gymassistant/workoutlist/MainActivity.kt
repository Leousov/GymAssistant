package com.example.gymassistant.workoutlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymassistant.R
import com.example.gymassistant.model.CWorkout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dataset = arrayOf(CWorkout("aaaa","asdasdasd"), CWorkout("asd","sdg"), CWorkout("ghjghj","qweqw"))
        val customAdapter = CRecyclerViewAdapterWorkout(dataset)

        val recyclerView: RecyclerView = findViewById(R.id.Recycler1)
        recyclerView.adapter = customAdapter
        val mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =  mLayoutManager
    }
    fun onButtonAddClick(view: View){
        val recyclerView: RecyclerView = findViewById(R.id.Recycler1)
    }
}
