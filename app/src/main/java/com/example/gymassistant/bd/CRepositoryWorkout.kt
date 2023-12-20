package com.example.gymassistant.bd

import android.content.Context
import com.example.gymassistant.CDatabase
import com.example.gymassistant.model.CExercise
import com.example.gymassistant.model.CWorkout
import kotlinx.coroutines.flow.Flow

class CRepositoryWorkout (
    context : Context
) {
    private val db = CDatabase.getDatabase(context)
    private val daoWorkout= db.daoWorkout()

    fun getAll(): Flow<List<CWorkout>> {
        return daoWorkout.getAll()
    }
    fun getById(
        id : String
    ) : Flow<CWorkout?>
    {
        return daoWorkout.getById(id)
    }
    suspend fun insert(
        land: CWorkout
    )
    {
        daoWorkout.insert(land)
    }

    suspend fun delete(
        land: CWorkout
    )
    {
        daoWorkout.delete(land)
    }
}
