package com.example.gymassistant.workoutlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymassistant.R
import com.example.gymassistant.databinding.WorkoutListItemBinding
import com.example.gymassistant.model.CWorkout

class CRecyclerViewAdapterWorkout(
    private val dataset: Array<CWorkout>
) : RecyclerView.Adapter<CRecyclerViewAdapterWorkout.ViewHolder>()  {

    class ViewHolder(private val itemBinding: WorkoutListItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(item:CWorkout){
            itemBinding.workout = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.workout_list_item, parent, false)
        val listItemBinding = WorkoutListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(listItemBinding)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(dataset[position])
    }


}