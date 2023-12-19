package com.example.gymassistant.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gymassistant.model.CWorkout
import kotlinx.coroutines.flow.Flow

@Dao
interface IDAOWorkout {
    @Query("SELECT * FROM workout")
    fun getAll()                            : Flow<List<CWorkout>>
    @Query("SELECT * FROM workout WHERE id = :id")
    fun getById(
        id                                  : String
    ) : Flow<CWorkout>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: CWorkout) //async
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<CWorkout>) //async
    @Update
    suspend fun update(workout: CWorkout)
    @Delete
    suspend fun delete(workout: CWorkout)
}