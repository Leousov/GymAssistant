package com.example.gymassistant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymassistant.model.CExercise
import com.example.gymassistant.repository.CRepositoryExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CViewModelExerciseList(
    application: Application,
    private val workoutId: String
) : AndroidViewModel(application){

    private val repositoryExercise = CRepositoryExercise(application)
    private var itemsFlow = MutableStateFlow<List<CExercise>>(
        emptyList()
    )
    val items: StateFlow<List<CExercise>> = itemsFlow.asStateFlow()
    init {
        viewModelScope.launch {
            repositoryExercise
                .getExercisesByWorkoutId(workoutId)
                .collect {newItems->
                    itemsFlow.update { newItems }
                }
        }
    }
    fun loadExercises(workout_id: String) {
        viewModelScope.launch {
            repositoryExercise
                .getExercisesByWorkoutId(workout_id)
                .collect { newItems ->
                    itemsFlow.update { newItems }
                }
        }
    }
    fun deleteItem(
        item: CExercise?
    )
    {
        item?:return
        kotlinx.coroutines.MainScope().launch {
            repositoryExercise.delete(item)
        }
    }
    fun editItem(
        id: String,
        title: String,
        text: String,
        user_id: String
    )
    {
    }
}