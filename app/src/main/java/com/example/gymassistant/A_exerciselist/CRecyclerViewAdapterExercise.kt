package com.example.gymassistant.A_exerciselist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymassistant.databinding.ExericeListItemBinding
import com.example.gymassistant.model.CExercise

// Адаптер для отображения списка упражнений в RecyclerView
class CRecyclerViewAdapterExercise(
//    private val dataset: MutableList<CExercise>,
    private val clickListener: (exercise: CExercise) -> Unit,
    private val deleteListener: (exercise: CExercise) -> Unit
) : ListAdapter<CExercise, CRecyclerViewAdapterExercise.ViewHolder>(ExerciseDiffCallback()) {

    // ViewHolder для хранения и управления элементами списка
    class ViewHolder(
        private val itemBinding: ExericeListItemBinding,
        private var clickListener: (exercise: CExercise) -> Unit,
        private val deleteListener: (exercise: CExercise) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            // Устанавливаем слушатели для кнопок редактирования и удаления
            itemBinding.buttonEdit.setOnClickListener {
                clickListener(itemBinding.exercise!!)
            }
            itemBinding.buttonDelete.setOnClickListener {
                deleteListener(itemBinding.exercise!!)
            }
        }

        // Привязываем данные к элементу списка
        fun bindItem(item: CExercise) {
            itemBinding.exercise = item
        }
    }

    // Создание новых ViewHolder'ов при создании элементов списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItemBinding = ExericeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listItemBinding, clickListener, deleteListener)
    }

    // Привязываем данные к ViewHolder'у для отображения на экране
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }
    class ExerciseDiffCallback : DiffUtil.ItemCallback<CExercise>() {
        override fun areItemsTheSame(oldItem: CExercise, newItem: CExercise): Boolean {
            return oldItem.id == newItem.id // Сравнение по уникальному идентификатору
        }

        override fun areContentsTheSame(oldItem: CExercise, newItem: CExercise): Boolean {
            return oldItem == newItem // Сравнение по содержимому
        }
    }
}
