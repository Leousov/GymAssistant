package com.example.gymassistant.viewmodel.exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymassistant.model.CExercise
import com.example.gymassistant.repository.CRepositoryExercise
import com.example.gymassistant.viewmodel.workout.orRandomString
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
    val duration = MutableStateFlow("")
    val num_sets = MutableStateFlow("")
    val weight = MutableStateFlow("")
    val times_per_set = MutableStateFlow("")

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
                    this@CViewModelExerciseDetail.duration.update { exercise.duration!!.toString() }
                    this@CViewModelExerciseDetail.num_sets.update { exercise.num_sets!!.toString() }
                    this@CViewModelExerciseDetail.weight.update { exercise.weight!!.toString() }
                    this@CViewModelExerciseDetail.times_per_set.update { exercise.times_per_set!!.toString() }

                    _initilized.update { true }
                }
            }
        }
//        this@CViewModelExerciseDetail.workout_id.update { workout_id }

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
                    duration.value.toInt(),
                    num_sets.value.toInt(),
                    weight.value.toInt(),
                    times_per_set.value.toInt()
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
                    duration.value.toInt(),
                    num_sets.value.toInt(),
                    weight.value.toInt(),
                    times_per_set.value.toInt()
                )
            )
        }
        return true
    }
}