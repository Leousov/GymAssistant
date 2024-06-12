package com.example.gymassistant.repository

import android.content.Context
import com.example.gymassistant.CDatabase
import com.example.gymassistant.model.CSession
import kotlinx.coroutines.flow.Flow

class CRepositorySession (
    context : Context
)  {
    private val db = CDatabase.getDatabase(context)
    private val daoSession = db.daoSession()
    fun getAll(): Flow<List<CSession>> {
        return daoSession.getAll()
    }

    fun getById(
        id: String
    ): Flow<CSession?> {
        return daoSession.getById(id)
    }

    suspend fun insert(
        session: CSession
    ) {
        daoSession.insert(session)
    }

    suspend fun delete(
        session: CSession
    ) {
        daoSession.delete(session)
    }

}