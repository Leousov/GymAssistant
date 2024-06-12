package com.example.gymassistant.A_workoutlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymassistant.databinding.WorkoutListItemBinding
import com.example.gymassistant.model.CWorkout

// Адаптер для отображения списка тренировок в RecyclerView
class CRecyclerViewAdapterWorkout(
//    private val dataset: MutableList<CWorkout>, // Список тренировок для отображения
//    private val clickListener: (workout: CWorkout) -> Unit // Обработчик нажатий на элементы списка
    private val clickListener               : (land : CWorkout?) -> Unit,
    private val deleteListener              : (land : CWorkout?) -> Unit
) : ListAdapter<CWorkout, CRecyclerViewAdapterWorkout.ViewHolder>(WorkoutDiffCallback()) {

    // Внутренний класс ViewHolder для хранения представлений элемента списка
    class ViewHolder(
        private val itemBinding: WorkoutListItemBinding, // Биндинг для элемента списка
        private var clickListener: (workout: CWorkout) -> Unit, // Обработчик нажатий
        private val deleteListener: (workout : CWorkout?) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            // Установка слушателя на кнопку "Edit" внутри элемента списка
            itemBinding.buttonEdit.setOnClickListener {
                clickListener(itemBinding.workout!!) // Вызов обработчика при нажатии на кнопку
            }
        }

        // Привязка данных к элементу списка
        fun bindItem(item: CWorkout) {
            itemBinding.workout = item // Установка данных тренировки в биндинг
        }
    }

    // Создание нового ViewHolder при создании нового элемента списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItemBinding = WorkoutListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(listItemBinding, clickListener, deleteListener)
    }

    // Возвращает количество элементов в списке
//    override fun getItemCount(): Int {
//        return dataset.size
//    }

    // Привязка данных к ViewHolder при отображении элемента списка
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    class WorkoutDiffCallback : DiffUtil.ItemCallback<CWorkout>() {
        override fun areItemsTheSame(oldItem: CWorkout, newItem: CWorkout): Boolean {
            return oldItem.id == newItem.id // Сравнение по уникальному идентификатору
        }

        override fun areContentsTheSame(oldItem: CWorkout, newItem: CWorkout): Boolean {
            return oldItem == newItem // Сравнение по содержимому
        }
    }
}
