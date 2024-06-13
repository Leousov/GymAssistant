package com.example.gymassistant.viewmodel.graf

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymassistant.model.CSessionExercise
import com.example.gymassistant.repository.CRepositoryExercise
import com.example.gymassistant.repository.CRepositorySession
import com.example.gymassistant.repository.CRepositorySessionExercise
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CViewModelGrafDetail (
    application  : Application
) : AndroidViewModel(application) {
    private val repositoryExercise  = CRepositoryExercise(application)
    private val repositorySession  = CRepositorySession(application)
    private val repositorySessionExercise  = CRepositorySessionExercise(application)
    val title = MutableStateFlow("")

    private val _initilized = MutableStateFlow(false)
    val initilized: StateFlow<Boolean>
            = _initilized.asStateFlow()
    private val _message = MutableSharedFlow<Int>(
        replay = 1,
        onBufferOverflow  = BufferOverflow.DROP_OLDEST
    )
    val message: SharedFlow<Int>
            = _message.asSharedFlow()
    fun setItem(id: String){
        viewModelScope.launch {
            repositoryExercise.getById(id).collect { exercise ->
                exercise?.let{
                    this@CViewModelGrafDetail.title.update { exercise.name.toString() }
                }
            }
        }
        _initilized.update { true }
    }
    fun getExercises(ExerciseID: String ): Flow<List<CSessionExercise>> {
        return repositorySessionExercise.getByExerciseID(ExerciseID)
    }

}