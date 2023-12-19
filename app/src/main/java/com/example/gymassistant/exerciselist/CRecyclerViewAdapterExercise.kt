package com.example.gymassistant.exerciselist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gymassistant.databinding.ExericeListItemBinding
import com.example.gymassistant.model.CExercise
import java.text.FieldPosition

class CRecyclerViewAdapterExercise (
    private val dataset: MutableList<CExercise>,
    private val clickListener: (exercise: CExercise) -> Unit,
    private val deleteListener: (exercise: CExercise) -> Unit

): RecyclerView.Adapter<CRecyclerViewAdapterExercise.ViewHolder>() {
    class ViewHolder(private val itemBinding: ExericeListItemBinding,
                     private var clickListener: (exercise: CExercise) -> Unit,
                     private val deleteListener: (exercise: CExercise) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.buttonEdit.setOnClickListener{
                clickListener(itemBinding.exercise!!)
            }
            itemBinding.buttonDelete.setOnClickListener{
                deleteListener(itemBinding.exercise!!)
            }

        }
        fun bindItem(item:CExercise){
            itemBinding.exercise = item
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItemBinding = ExericeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(listItemBinding, clickListener, deleteListener)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(dataset[position])
    }
}