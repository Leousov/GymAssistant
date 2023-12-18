package com.example.gymassistant.workoutlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gymassistant.databinding.ActivityCworkoutDetailsBinding
import com.example.gymassistant.model.CWorkout

class CRecyclerViewAdapterWorkout(
    private val dataset: MutableList<CWorkout>
) : RecyclerView.Adapter<CRecyclerViewAdapterWorkout.ViewHolder>()  {

    class ViewHolder(private val itemBinding: ActivityCworkoutDetailsBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(item:CWorkout){
            itemBinding.workout = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItemBinding = ActivityCworkoutDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(listItemBinding)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(dataset[position])
    }


}