package com.example.gymassistant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymassistant.model.CWorkout
import com.example.gymassistant.repository.CRepositoryWorkout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CViewModelWorkoutList(
    application: Application
) : AndroidViewModel(application){

    private val repositoryWorkout = CRepositoryWorkout(application)
    private var itemsFlow = MutableStateFlow<List<CWorkout>>(
        emptyList()
    )
    val items: StateFlow<List<CWorkout>> = itemsFlow.asStateFlow()
    init {
        viewModelScope.launch {
            repositoryWorkout
                .getAll()
                .collect {newItems->
                    itemsFlow.update { newItems }
                }
        }
    }
    fun deleteItem(
        item: CWorkout?
    )
    {
        item?:return
        kotlinx.coroutines.MainScope().launch {
            repositoryWorkout.delete(item)
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