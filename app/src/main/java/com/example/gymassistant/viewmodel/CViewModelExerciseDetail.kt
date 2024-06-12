package com.example.gymassistant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymassistant.model.CExercise
import com.example.gymassistant.repository.CRepositoryExercise
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CViewModelExerciseDetail(
    application  : Application
) : AndroidViewModel(application) {
    private val repositoryExercise  = CRepositoryExercise(application)
    val id = MutableStateFlow("")
    val workout_id = MutableStateFlow("")
    val name = MutableStateFlow("")
    val description = MutableStateFlow("")
    val duration = MutableStateFlow(-1)
    val num_sets = MutableStateFlow(-1)
    val weight = MutableStateFlow(-1)
    val times_per_set = MutableStateFlow(-1)

    private val _initilized = MutableStateFlow(false)
    val initilized: StateFlow<Boolean>
            = _initilized.asStateFlow()
    private val _message = MutableSharedFlow<Int>(
        replay = 1,
        onBufferOverflow  = BufferOverflow.DROP_OLDEST
    )
    val message: SharedFlow<Int>
            = _message.asSharedFlow()
    fun setItem(id: String, workout_id: String){
    viewModelScope.launch {
        repositoryExercise.getById(id).collect { exercise ->
                exercise?.let{
                    this@CViewModelExerciseDetail.id.update { exercise.id.toString() }
                    this@CViewModelExerciseDetail.workout_id.update { exercise.workout_id.toString() }
                    this@CViewModelExerciseDetail.name.update { exercise.name.toString() }
                    this@CViewModelExerciseDetail.description.update { exercise.description.toString() }
                    this@CViewModelExerciseDetail.duration.update { exercise.duration!! }
                    this@CViewModelExerciseDetail.num_sets.update { exercise.num_sets!! }
                    this@CViewModelExerciseDetail.weight.update { exercise.weight!! }
                    this@CViewModelExerciseDetail.times_per_set.update { exercise.times_per_set!! }
                }
            }
        }
        this@CViewModelExerciseDetail.workout_id.update { workout_id }
        _initilized.update { true }
    }
    fun save(): Boolean
    {
        //Вызвать сохранение в БД.
        kotlinx.coroutines.MainScope().launch {
            repositoryExercise.insert(
                CExercise(
                    id.orRandomString,
                    workout_id.value,
                    name.value,
                    description.value,
                    duration.value,
                    num_sets.value,
                    weight.value,
                    times_per_set.value
                )
            )
        }
        return true
    }
    fun delete(): Boolean {
        // Вызвать удаление из БД.
        kotlinx.coroutines.MainScope().launch {
            repositoryExercise.delete(
                CExercise(
                    id.value,
                    workout_id.value,
                    name.value,
                    description.value,
                    duration.value,
                    num_sets.value,
                    weight.value,
                    times_per_set.value
                )
            )
        }
        return true
    }
}