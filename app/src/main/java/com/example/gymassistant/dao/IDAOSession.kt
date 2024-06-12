package com.example.gymassistant.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gymassistant.model.CSession
import kotlinx.coroutines.flow.Flow

@Dao
interface IDAOSession {
    @Query("SELECT * FROM session")
    fun getAll(): Flow<List<CSession>>

    @Query("SELECT * FROM session WHERE id = :id")
    fun getById(
        id: String
    ): Flow<CSession>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: CSession)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sessions: List<CSession>)

    @Update
    suspend fun update(session: CSession)

    @Delete
    suspend fun delete(session: CSession)
}
