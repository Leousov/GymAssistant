package com.example.gymassistant.viewmodel.exercise

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CViewModelExerciseListFactory(
    private val application: Application,
    private val workoutId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CViewModelExerciseList::class.java)) {
            return CViewModelExerciseList(application, workoutId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}