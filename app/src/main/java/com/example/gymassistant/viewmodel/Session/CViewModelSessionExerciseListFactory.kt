package com.example.gymassistant.viewmodel.Session

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CViewModelSessionExerciseListFactory(
    private val application: Application,
    private val workout_id: String,
    private val session_id: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CViewModelSessionExerciseList::class.java)) {
            return CViewModelSessionExerciseList(application, workout_id, session_id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}