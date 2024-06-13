package com.example.gymassistant.A_sessionlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymassistant.databinding.SessionExerciseListItemBinding
import com.example.gymassistant.model.CSessionExercise
import com.example.gymassistant.viewmodel.session.CViewModelSessionExerciseList

// Адаптер для отображения списка упражнений в RecyclerView
class CRecyclerViewAdapterSessionExercise(
    private val viewModel: CViewModelSessionExerciseList,
    private val ListenerUp: (session_exercise: CSessionExercise) -> Unit,
    private val ListenerDown: (session_exercise: CSessionExercise) -> Unit
) : ListAdapter<CSessionExercise, CRecyclerViewAdapterSessionExercise.ViewHolder>(SessionExerciseDiffCallback()) {
    init {
        // Сохраняем данные сразу после их получения
        viewModel.insertAll(currentList)
    }
    // ViewHolder для хранения и управления элементами списка
    class ViewHolder(
        private val itemBinding: SessionExerciseListItemBinding,
        private val ListenerUp: (session_exercise: CSessionExercise) -> Unit,
        private val ListenerDown: (session_exercise: CSessionExercise) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            // Устанавливаем слушатели для кнопок редактирования и удаления
            itemBinding.buttonUp.setOnClickListener {
                ListenerUp(itemBinding.sessionExercise!!)
                updateText(itemBinding.sessionExercise!!)
            }
            itemBinding.buttonDown.setOnClickListener {
                ListenerDown(itemBinding.sessionExercise!!)
                updateText(itemBinding.sessionExercise!!)
            }
        }

        // Привязываем данные к элементу списка
        fun bindItem(item: CSessionExercise) {
            itemBinding.sessionExercise = item
        }
        private fun updateText(item: CSessionExercise) {
            itemBinding.exerciseSets.text = "Done: ${item.sets_done}/${item.num_sets}"
        }
    }

    // Создание новых ViewHolder'ов при создании элементов списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItemBinding = SessionExerciseListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listItemBinding, ListenerUp, ListenerDown)
    }

    // Привязываем данные к ViewHolder'у для отображения на экране
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }
    class SessionExerciseDiffCallback : DiffUtil.ItemCallback<CSessionExercise>() {
        override fun areItemsTheSame(oldItem: CSessionExercise, newItem: CSessionExercise): Boolean {
            return oldItem.id == newItem.id // Сравнение по уникальному идентификатору
        }

        override fun areContentsTheSame(oldItem: CSessionExercise, newItem: CSessionExercise): Boolean {
            return oldItem == newItem // Сравнение по содержимому
        }
    }
}
