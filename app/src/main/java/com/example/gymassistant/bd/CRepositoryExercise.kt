package com.example.gymassistant.bd

import android.content.Context
import com.example.gymassistant.CDatabase
import com.example.gymassistant.model.CExercise
import kotlinx.coroutines.flow.Flow

class CRepositoryExercise (
    context : Context
) {
    private val db = CDatabase.getDatabase(context)
    private val daoExercise= db.daoExercise()

    fun getAll(): Flow<List<CExercise>> {
        return daoExercise.getAll()
    }
    fun getById(
        id : String
    ) : Flow<CExercise?>
    {
        return daoExercise.getById(id)
    }
    suspend fun insert(
        land: CExercise
    )
    {
        daoExercise.insert(land)
    }

    suspend fun delete(
        land: CExercise
    )
    {
        daoExercise.delete(land)
    }
}
