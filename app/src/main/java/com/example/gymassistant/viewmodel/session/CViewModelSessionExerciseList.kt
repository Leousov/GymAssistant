package com.example.gymassistant.viewmodel.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymassistant.model.CSessionExercise
import com.example.gymassistant.repository.CRepositorySessionExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CViewModelSessionExerciseList(
    application: Application,
    private val workout_id: String,
    private val session_id: String
) : AndroidViewModel(application){
    private val repositorySessionExercise = CRepositorySessionExercise(application)
    private var itemsFlow = MutableStateFlow<List<CSessionExercise>>(
        emptyList()
    )
    val items: StateFlow<List<CSessionExercise>> = itemsFlow.asStateFlow()
    init {
        viewModelScope.launch {
            repositorySessionExercise
                .generateExercisesByWorkoutId(workout_id, session_id)
                .collect {newItems->
                    itemsFlow.update { newItems }
                    repositorySessionExercise.insertAll(newItems)
                }
        }
    }
    fun update(
        item: CSessionExercise?
    )
    {
        item?:return
        kotlinx.coroutines.MainScope().launch {
            repositorySessionExercise.update(item)
        }
    }
    fun insertAll(sessionExercises: List<CSessionExercise>) = viewModelScope.launch {
        repositorySessionExercise.insertAll(sessionExercises)
    }
    fun deleteItem(
        item: CSessionExercise?
    )
    {
        item?:return
        kotlinx.coroutines.MainScope().launch {
            repositorySessionExercise.delete(item)
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