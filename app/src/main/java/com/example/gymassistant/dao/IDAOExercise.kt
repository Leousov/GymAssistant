package com.example.gymassistant.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.gymassistant.model.CExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface IDAOExercise {
    @Query("SELECT * FROM exercise")
    fun getAll()                            : Flow<List<CExercise>>
    @Query("SELECT * FROM exercise WHERE id = :id")
    fun getById(
        id                                  : String
    ) : Flow<CExercise>

    @Insert(onConflict = REPLACE)
    suspend fun insert(exercise: CExercise) //async
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(exercises: List<CExercise>) //async
    @Update
    suspend fun update(exercise: CExercise)
    @Delete
    suspend fun delete(exercise: CExercise)
}
