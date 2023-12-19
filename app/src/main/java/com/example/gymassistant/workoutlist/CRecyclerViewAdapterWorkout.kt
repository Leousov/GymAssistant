package com.example.gymassistant.workoutlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gymassistant.databinding.WorkoutListItemBinding
import com.example.gymassistant.model.CWorkout

class CRecyclerViewAdapterWorkout(
    private val dataset: MutableList<CWorkout>,
    private val clickListener: (workout: CWorkout) -> Unit
) : RecyclerView.Adapter<CRecyclerViewAdapterWorkout.ViewHolder>()  {

    class ViewHolder(private val itemBinding: WorkoutListItemBinding,
                     private var clickListener: (workout: CWorkout) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.buttonEdit.setOnClickListener{
                clickListener(itemBinding.workout!!)
            }
        }
        fun bindItem(item:CWorkout){
            itemBinding.workout = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItemBinding = WorkoutListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(listItemBinding, clickListener)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(dataset[position])
    }


}