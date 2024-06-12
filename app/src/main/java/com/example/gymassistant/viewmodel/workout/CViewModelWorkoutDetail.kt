package com.example.gymassistant.viewmodel.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymassistant.A_workoutlist.getRandomString
import com.example.gymassistant.model.CWorkout
import com.example.gymassistant.repository.CRepositoryWorkout
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

val StateFlow<String>.orRandomString: String
    get() = if (value.isEmpty()) getRandomString(32) else value
class CViewModelWorkoutDetail(
    application  : Application
) : AndroidViewModel(application) {
    private val repositoryWorkout  = CRepositoryWorkout(application)
    val title = MutableStateFlow("")
    val id = MutableStateFlow("")
    val text = MutableStateFlow("")
    val user_id = MutableStateFlow("")

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
        repositoryWorkout.getById(id).collect { workout ->
                workout?.let{
                    this@CViewModelWorkoutDetail.id.update { workout.id.toString() }
                    this@CViewModelWorkoutDetail.text.update { workout.text.toString() }
                    this@CViewModelWorkoutDetail.title.update { workout.title.toString() }
                    this@CViewModelWorkoutDetail.user_id.update { workout.user_id.toString() }
                }
            }
        }
        _initilized.update { true }
    }
    fun save(): Boolean
    {
        //Вызвать сохранение в БД.
        kotlinx.coroutines.MainScope().launch {
            repositoryWorkout.insert(
                CWorkout(
                    id.orRandomString,
                    title.value,
                    text.value,
                    user_id.value,
                )
            )
        }
        return true
    }
    fun delete(): Boolean {
        // Вызвать удаление из БД.
        kotlinx.coroutines.MainScope().launch {
            repositoryWorkout.delete(
                CWorkout(
                    id.value,
                    title.value,
                    text.value,
                    user_id.value,
                )
            )
        }
        return true
    }
}