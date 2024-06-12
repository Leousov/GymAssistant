package com.example.gymassistant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymassistant.model.CSession
import com.example.gymassistant.repository.CRepositorySession
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

class CViewModelSessionDetail(
    application  : Application
) : AndroidViewModel(application)  {
    private val repositorySession  = CRepositorySession(application)
    private val repositoryWorkout  = CRepositoryWorkout(application)
    val workout_id = MutableStateFlow("")
    val id = MutableStateFlow("")
    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
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
            repositorySession.getById(id).collect { session ->
                session?.let{
                    this@CViewModelSessionDetail.id.update { session.id.toString() }
                    this@CViewModelSessionDetail.workout_id.update { session.workout_id.toString() }
                }
            }
        }
        this@CViewModelSessionDetail.workout_id.update { workout_id }
        viewModelScope.launch {
            repositoryWorkout.getById(workout_id).collect { workout ->
                workout?.let{
                    this@CViewModelSessionDetail.title.update { workout.title.toString() }
                    this@CViewModelSessionDetail.description.update { workout.text.toString() }
                }
            }
        }
        _initilized.update { true }
    }
    fun save(): Boolean
    {
        //Вызвать сохранение в БД.
        kotlinx.coroutines.MainScope().launch {
            repositorySession.insert(
                CSession(
                    id.orRandomString,
                    workout_id.value
                )
            )
        }
        return true
    }
    fun delete(): Boolean {
        // Вызвать удаление из БД.
        kotlinx.coroutines.MainScope().launch {
            repositorySession.delete(
                CSession(
                    id.value,
                    workout_id.value
                )
            )
        }
        return true
    }
}